# Отчет по лабораторной работе 1

## Тема

Подготовка репозитория и создание простого Spring Boot проекта.

## Цель работы

Создать Java-проект с использованием Spring Boot версии 3.5.5 или выше, опубликовать его на GitHub и добавить простые контроллеры для проверки работы приложения.

## Используемые инструменты

- IntelliJ IDEA
- JDK 25
- Maven Wrapper
- Spring Boot 3.5.5
- GitHub

Проверка окружения:

```bash
java --version
javac --version
.\mvnw.cmd -v
```

В результате были доступны JDK 25, `javac` 25 и Maven 3.9.14 через Maven Wrapper.

## Ход выполнения

Я создал Maven-проект на Spring Boot. Для проекта были выбраны следующие параметры:

- Group: `ru.vako`
- Artifact: `rbpovako`
- Package: `ru.vako.rbpovako`
- Java: 21 в настройках проекта
- Spring Boot: 3.5.5
- Dependency: Spring Web

Основной класс приложения:

```text
src/main/java/ru/vako/rbpovako/RbpovakoApplication.java
```

Для проверки работы приложения я добавил простой REST-контроллер:

```text
src/main/java/ru/vako/rbpovako/controller/IndexController.java
```

В контроллере реализованы два endpoint:

| Метод | URL | Назначение |
| --- | --- | --- |
| GET | `/` | Возвращает название сервиса и тему |
| GET | `/health` | Возвращает статус приложения |

## Проверка

Для проверки сборки и тестов была выполнена команда:

```bash
.\mvnw.cmd clean test
```

Тесты завершились успешно:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

После этого был собран jar-файл:

```bash
.\mvnw.cmd package -DskipTests
```

Собранный файл:

```text
target/rbpovako-0.0.1-SNAPSHOT.jar
```

Приложение было запущено командой:

```bash
java -jar target/rbpovako-0.0.1-SNAPSHOT.jar
```

Проверка endpoint `/health` показала:

```json
{
  "status": "ok"
}
```

Проверка endpoint `/` показала:

```json
{
  "service": "rbpovako",
  "theme": "recruiting"
}
```

## Результат

В результате лабораторной работы был создан Spring Boot проект `rbpovako`, добавлены простые контроллеры и проверена работа приложения через HTTP-запросы. Проект успешно собирается и запускается.

