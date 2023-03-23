import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import io.gitlab.arturbosch.detekt.Detekt
plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.gitlab.arturbosch.detekt").version("1.22.0") // This is to add detekt
    id("jacoco")// Add Jacoco
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.hrv.mart"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    detektPlugins ("io.gitlab.arturbosch.detekt:detekt-formatting:1.22.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
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
            excludes = listOf(
                "com.hrv.mart.user.repository.UserRepository.kt.*"
            )
            limit {
                minimum = "0.9".toBigDecimal()
            }
        }
    }
}
tasks.jacocoTestReport{
    reports {
        html.required.set(true)
        generate()
    }
}
