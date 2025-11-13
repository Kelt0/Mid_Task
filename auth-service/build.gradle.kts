buildscript {
    repositories {
        gradlePluginPortal()
        maven("https://packages.confluent.io/maven/")
    }
}

    plugins {
        java
        id("org.springframework.boot") version "3.5.6"
        id("io.spring.dependency-management") version "1.1.7"
        id("com.github.imflog.kafka-schema-registry-gradle-plugin") version "2.4.1"
    }

    group = "org.example"
    version = "0.0.1-SNAPSHOT"
    description = "auth-service"

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
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
        runtimeOnly("org.postgresql:postgresql")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("io.jsonwebtoken:jjwt-api:0.13.0")
        runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
        runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
        implementation("org.springframework.kafka:spring-kafka")
        implementation("io.confluent:kafka-avro-serializer:7.9.0")
        testImplementation("io.confluent:kafka-schema-registry:7.9.0")
        implementation("jakarta.validation:jakarta.validation-api:3.1.1")
        implementation("org.apache.avro:avro:1.12.1")
        implementation(platform("io.jsonwebtoken:jjwt-bom:0.13.0"))
    }

// конвертация avdl в avsc
tasks.register<Exec>("convertAvdlToAvsc") {
    commandLine(
        "java", "-jar", "libs/avro-tools-1.12.1.jar",
        "idl2schemata", "src/main/avro/UserCreatedEvent.avdl",
        "src/main/resources/avro"
    )
}

// генерация java файлов на основе avsc
tasks.register<Exec>("generateJavaFromAvsc") {
//    group = "avro"
//    description = "Generate Java classes from .avsc"
//    classpath = sourceSets.main.get().runtimeClasspath
//    mainClass.set("org.apache.avro.tool.Main")
//    args = listOf(
//        "compile", "schema",
//        "src/main/resources/avro/UserCreatedEvent.avsc",
//        "src/main/generated"
//    )
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

    tasks.withType<Test> {
        useJUnitPlatform()
    }

