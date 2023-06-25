import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt
plugins {
    id("org.springframework.boot") version "3.1.1"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt").version("1.22.0") // This is to add detekt
    id("jacoco")// Add Jacoco
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.hrv.mart"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/hrv-mart/custom-pageable")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    detektPlugins ("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    // User Model
    implementation("com.hrv.mart:user-library:0.0.3")
    // Auth Library
    implementation("com.hrv.mart:auth-library:0.0.2")
    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    implementation("io.projectreactor.kafka:reactor-kafka")
    // Appwrite
    implementation("io.appwrite:sdk-for-kotlin:2.0.0")
}
detekt {
    toolVersion = "1.22.0"
    config = files("config/detekt/detekt.yml")
}
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestCoverageVerification")
}
/*
* Jacoco configs*/
tasks.jacocoTestCoverageVerification {

    violationRules {
        rule {
//            enabled=true
//            element="CLASS"
//            excludes= listOf("**/*Repository.kt")
//            limit {
//                minimum = "0.9".toBigDecimal()
//            }
        }
    }
}
tasks.jacocoTestReport{
    reports {
        html.required.set(true)
        generate()
    }
//    afterEvaluate {
//        classDirectories.setFrom(files(classDirectories.files.toCollection(
//            fileTree()
//        )))
//
}
