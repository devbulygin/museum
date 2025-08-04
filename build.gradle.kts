plugins {
	java
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("checkstyle")
	id ("org.openapi.generator") version "7.14.0"
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

openApiGenerate {
	generatorName.set("java")
	inputSpec.set("$rootDir/src/main/resources/static/museum-v3.0.yaml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("org.openapi.example.api")
	invokerPackage.set("org.openapi.example.invoker")
	modelPackage.set("org.openapi.example.model")
	configOptions.put("dateLibrary", "java8")
}
