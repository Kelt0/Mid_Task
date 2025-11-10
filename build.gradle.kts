plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    id("jacoco")
    id("checkstyle")
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "Middle_Task"

checkstyle {
    toolVersion = "10.12.7"
    configFile = file("${rootProject.projectDir}/config/checkstyle/checkstyle.xml")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

jacoco {
    toolVersion = "0.8.11"
}

tasks.withType<Test> {
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named<Test>("test"))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn(tasks.named("jacocoTestReport"))

    // Правила проверки
    violationRules {
        rule {
            element = "CLASS" // Проверяем покрытие по классам

            // Проверяем покрытие по Инструкциям (INSTRUCTION)
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                // Требуемое минимальное покрытие: 0.80 = 80%
                minimum = "0.80".toBigDecimal()
            }
        }
    }
}

// 4. Финальный штрих: Заставляем `check` зависеть от нашей проверки
// Теперь `./gradlew check` будет запускать и проверку покрытия
tasks.named("check") {
    dependsOn(tasks.named("jacocoTestCoverageVerification"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
