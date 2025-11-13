pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

rootProject.name = "Middle_Task"
include(
    "api-gateway",
    "auth-service",
    "inventory-service",
    "notification-service",
    "order-service",
    "product-service",
    "user-service",
    "kafka-event-schema"
)

project(":kafka-event-schema").projectDir = file("kafka-event-schema")