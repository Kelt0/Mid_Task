    plugins {
        java
        id("org.springframework.boot") version "3.5.6"
        id("io.spring.dependency-management") version "1.1.7"
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
                java.srcDir("kafka-event-schema/src/main/generated")
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
        implementation("jakarta.validation:jakarta.validation-api:3.1.1")
        implementation("org.apache.avro:avro:1.12.1")
        implementation(platform("io.jsonwebtoken:jjwt-bom:0.13.0"))
        implementation(project(":kafka-event-schema"))
    }


    tasks.withType<Test> {
        useJUnitPlatform()
    }

