plugins {
    kotlin("jvm") version "1.4.0"
    id("com.github.johnrengelman.shadow") version "6.1.0"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "com.ctl.home"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

extra["feignVersion"] = "11.0"
extra["serializationVersion"] = "1.0.0-RC"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.springframework.cloud:spring-cloud-openfeign-core:3.0.0")
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.3.4.RELEASE")
    implementation("io.github.openfeign:feign-core:${project.extra["feignVersion"]}")
    implementation("io.github.openfeign:feign-jackson:${project.extra["feignVersion"]}")
    implementation("io.github.openfeign:feign-okhttp:${project.extra["feignVersion"]}")
    implementation("io.github.openfeign:feign-slf4j:${project.extra["feignVersion"]}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${project.extra["serializationVersion"]}")
    implementation("com.charleskorn.kaml:kaml:0.20.0")

    implementation("com.pi4j:pi4j-core:1.2")
    implementation("com.pi4j:pi4j-device:1.2")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.0")
    testImplementation("junit:junit:4.12")
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
//        archiveBaseName.set("shadow")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "com.ctl.home.tado.TadoSensorApp"))
        }
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}