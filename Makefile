# Makefile
.PHONY: up down build

# Запустить всю инфраструктуру
up:
	docker-compose up -d

# Остановить всю инфраструктуру
down:
	docker-compose down

# Собрать все микросервисы
build:
	./gradlew build