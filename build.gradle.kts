import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
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

fun sanitizeFileName(fileName: String): String {
	return fileName.replace(".yaml", "").replace(".yml", "")
		.replace(".", "_").replace("-", "_")
		.lowercase().replace(Regex("[^a-z0-9_]"), "_")
}

val openApiSpecs = mutableMapOf<String, String>()
val specsDir = File(openApiSpecsDir)

if (specsDir.exists()) {
	specsDir.listFiles()?.forEach { file ->
		if (file.name.endsWith(".yaml") || file.name.endsWith(".yml")) {
			val sanitizedName = sanitizeFileName(file.name)
			openApiSpecs[sanitizedName] = file.path
		}
	}
}

openApiSpecs.forEach { (key, specPath) ->
	tasks.register<GenerateTask>("openApiGenerate-$key") {
		generatorName.set("spring")
		inputSpec.set(specPath)
		outputDir.set("$outputDir/$key")
		apiPackage.set("com.museum.generated.api.$key")
		modelPackage.set("com.museum.generated.model.$key")

		configOptions.set(mapOf(
			"dateLibrary" to "java8",
			"interfaceOnly" to "true",
			"delegatePattern" to "true",
			"useTags" to "true",
			"skipDefaultInterface" to "true",
			"sourceFolder" to "src/main/java",
			"useSpringBoot3" to "true"
		))

		globalProperties.set(mapOf(
			"apis" to "",
			"models" to ""
		))
	}
}

tasks.register("generateAllOpenApi") {
	group = "openapi tools"
	description = "Генерирует все OpenAPI контроллеры"
	dependsOn("cleanGeneratedOpenApi")
	dependsOn(openApiSpecs.keys.map { "openApiGenerate-$it" })
}

tasks.register<Delete>("cleanGeneratedOpenApi") {
	group = "openapi tools"
	description = "Очищает сгенерированные OpenAPI файлы"
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