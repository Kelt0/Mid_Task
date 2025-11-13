buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://packages.confluent.io/maven/")
    }
}

plugins {
    id("java")
    id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "2.4.1"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    sourceSets {
        val main by getting {
            java.srcDir("src/main/generated")
        }
    }
}

repositories {
    mavenCentral()
    maven(url = "https://packages.confluent.io/maven")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("io.confluent:kafka-avro-serializer:7.9.0")
    testImplementation("io.confluent:kafka-schema-registry:7.9.0")
    implementation("org.apache.avro:avro:1.12.1")
}

// конвертация avdl в avsc
tasks.register<Exec>("convertAvdlToAvsc") {
    commandLine(
        "java", "-jar", "libs/avro-tools-1.12.1.jar",
        "idl2schemata", "src/main/avro/UserCreatedEvent.avdl",
        "src/main/resources/avro"
    )
}

tasks.register<Exec>("generateJavaFromAvsc") {
    commandLine(
        "java", "-jar", "libs/avro-tools-1.12.1.jar",
        "compile", "schema",
        "src/main/resources/avro/UserCreatedEvent.avsc",
        "src/main/generated"
    )
}

// регистрация схемы
schemaRegistry {
    url = "http://localhost:8800"
    register {
        subject("user-created", "src/main/resources/avro/UserCreatedEvent.avsc", "AVRO")
    }
}


tasks.test {
    useJUnitPlatform()
}