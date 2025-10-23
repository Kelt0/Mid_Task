# Используем многоэтапную сборку для оптимизации

# --- ЭТАП 1: Сборка ---
# Берем образ с полным JDK и Gradle для сборки нашего приложения
FROM eclipse-temurin:17-jdk AS builder

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем сначала файлы сборки, чтобы использовать кэш Docker
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

# Копируем исходный код нашего сервиса
COPY src ./src

# Запускаем сборку. bootJar - стандартная задача Spring Boot для создания исполняемого jar
RUN ./gradlew bootJar --no-daemon

# --- ЭТАП 2: Запуск ---
# Берем минимальный образ только с Java Runtime Environment (JRE), он меньше и безопаснее
FROM eclipse-temurin:17-jre

WORKDIR /app

# Копируем ТОЛЬКО собранный jar-файл из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar

# Указываем команду, которая будет выполняться при старте контейнера
ENTRYPOINT ["java", "-jar", "app.jar"]