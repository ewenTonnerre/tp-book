import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	id ("info.solidsoft.pitest") version "1.15.0"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

sourceSets {
	create("testIntegration") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}

	create("testComponent") {
		compileClasspath += sourceSets.main.get().output
		runtimeClasspath += sourceSets.main.get().output
	}
}

val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

val testComponentImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	implementation("org.postgresql:postgresql")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testImplementation("net.jqwik:jqwik-spring:0.10.0")
	testImplementation("io.mockk:mockk:1.13.8")
	testImplementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.15.0")

	testIntegrationImplementation("io.mockk:mockk:1.13.8")
	testIntegrationImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testIntegrationImplementation("org.testcontainers:postgresql:1.19.1")
	testIntegrationImplementation("org.testcontainers:junit-jupiter:1.19.1")
	testIntegrationImplementation("org.testcontainers:jdbc-test:1.12.0")
	testIntegrationImplementation("org.testcontainers:testcontainers:1.19.1")
	testIntegrationImplementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.15.0")

	testComponentImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
	testComponentImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testComponentImplementation("io.cucumber:cucumber-java:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-spring:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-junit:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.0")
	testComponentImplementation("io.rest-assured:rest-assured:5.3.2")
	testComponentImplementation("org.junit.platform:junit-platform-suite:1.10.0")
	testComponentImplementation("org.testcontainers:postgresql:1.19.1")
	testComponentImplementation("org.testcontainers:junit-jupiter:1.19.1")
	testComponentImplementation("com.willowtreeapps.assertk:assertk:0.27.0")
	testComponentImplementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.15.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<Test>("testIntegration") {
	useJUnitPlatform()
	testClassesDirs = sourceSets["testIntegration"].output.classesDirs
	classpath = sourceSets["testIntegration"].runtimeClasspath
}

task<Test>("testComponent") {
	useJUnitPlatform()
	testClassesDirs = sourceSets["testComponent"].output.classesDirs
	classpath = sourceSets["testComponent"].runtimeClasspath
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
}

jacoco {
	toolVersion = "0.8.9"
	reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
		csv.required = false
		html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
	}
}

pitest {
	junit5PluginVersion.set("1.2.0")
	avoidCallsTo.set(setOf("kotlin.jvm.internal"))
	mutators.set(setOf("STRONGER"))
	targetClasses.set(setOf("com.example.*"))
	threads.set(Runtime.getRuntime().availableProcessors())
	outputFormats.set(setOf("XML", "HTML"))
	mutationThreshold.set(0)
	coverageThreshold.set(0)
}
