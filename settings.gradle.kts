pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Middle_Task"
include(
    "api-gateway",
    "auth-service",
    "inventory-service",
    "notification-service",
    "order-service",
    "production-service",
    "user-service"
)