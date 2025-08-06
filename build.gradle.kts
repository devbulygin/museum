import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import com.google.common.base.CaseFormat
import java.io.File

plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("checkstyle")
	id("org.openapi.generator") version "7.14.0"
	jacoco
}

group = "com.museum"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql:42.7.7")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.flywaydb:flyway-database-postgresql:10.12.0")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

checkstyle {
	toolVersion = "10.12.4"
	configFile = file("config/checkstyle/checkstyle.xml")
	isIgnoreFailures = false
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

jacoco {
	toolVersion = "0.8.13"
	reportsDirectory = layout.buildDirectory.dir("jacoco")
}

val openApiSpecsDir = "${layout.projectDirectory}/src/main/resources/static"
val outputDir = "${layout.buildDirectory.get().asFile.absolutePath}/generated"

fun convertYamlCamelCaseToSnakeCase(str: String): String {
	val strWithoutPostfix = str.replace(".yaml", "").replace(".yml", "").trim()
	val cleanName = strWithoutPostfix.replace(".", "_").replace("-", "_")
	return try {
		CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cleanName)
	} catch (e: Exception) {
		cleanName.lowercase().replace(Regex("[^a-z0-9_]"), "_")
	}
}

val openApiSpecs = mutableMapOf<String, String>()
val specsDir = File(openApiSpecsDir)

if (specsDir.exists()) {
	specsDir.listFiles()?.forEach { file ->
		if (file.name.endsWith(".yaml") || file.name.endsWith(".yml")) {
			val convertedFileName = convertYamlCamelCaseToSnakeCase(file.name)
			openApiSpecs[convertedFileName] = file.path
		}
	}
}

openApiSpecs.forEach { (key, specPath) ->
	val taskOutputDir = "$outputDir/$key"

	tasks.register<GenerateTask>("openApiGenerate-$key") {
		generatorName.set("spring")
		inputSpec.set(specPath)
		outputDir.set(taskOutputDir)
		apiPackage.set("com.museum.generated.api.$key")
		invokerPackage.set("com.museum.generated.invoker.$key")
		modelPackage.set("com.museum.generated.model.$key")

		configOptions.set(mapOf(
			"dateLibrary" to "java8",
			"interfaceOnly" to "true",
			"delegatePattern" to "true",
			"useTags" to "true",
			"skipDefaultInterface" to "true",
			"sourceFolder" to "src/main/java",
			"useSpringBoot3" to "true",
			"library" to "spring-boot"
		))

		globalProperties.set(mapOf(
			"apis" to "",
			"models" to "",
			"supportingFiles" to ""
		))
	}
}

tasks.register("generateAllOpenApi") {
	group = "openapi"
	description = "Генерирует все OpenAPI контроллеры из спецификаций"
	dependsOn("cleanGeneratedOpenApi")
	dependsOn(openApiSpecs.keys.map { "openApiGenerate-$it" })

	doFirst {
		openApiSpecs.forEach { (key, path) ->
			println("  $key -> $path")
		}
	}
}

tasks.register<Delete>("cleanGeneratedOpenApi") {
	group = "openapi"
	description = "Очищает все сгенерированные OpenAPI файлы"
	delete(outputDir)
}

sourceSets {
	main {
		java {
			openApiSpecs.keys.forEach { key ->
				srcDir("$outputDir/$key/src/main/java")
			}
		}
	}
}

tasks.compileJava {
	dependsOn("generateAllOpenApi")
}

tasks.named("processResources") {
	dependsOn("generateAllOpenApi")
}