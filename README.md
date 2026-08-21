# Система управления банковскими картами

REST API для управления банковскими картами и транзакциями с ролевой моделью доступа и JWT-аутентификацией.

## 📦 Технологический стек

- **Язык**: Java 21
- **Фреймворк**: Spring Boot 3.4.6
- **База данных**: PostgreSQL 14
- **Миграции**: Liquibase
- **Безопасность**: JWT аутентификация, AES-256-GCM шифрование
- **Инструменты**:
  - Spring Data JPA
  - Spring Security
  - SpringDoc OpenAPI
  - Lombok
  - Maven
  - Docker + Docker Compose

## 🏗️ Архитектура

Проект построен по принципам **Clean Architecture (Hexagonal Architecture)** с разделением ответственности по слоям. Модуль `Card` полностью переведён на новую архитектуру.

```text
src/main/java/com/bank/cards/
├── domain/                          # Ядро бизнес-логики (без зависимостей от фреймворков)
│   ├── entity/                      # Доменные сущности (BankCard, Transaction)
│   ├── valueobject/                 # Value Objects (CardId, UserId, TransactionId, TransactionStatus)
│   ├── exception/                   # Доменные исключения (Card..., Transaction...)
│   ├── port/                        # Порты (интерфейсы для инфраструктуры)
│   └── repository/                  # Интерфейсы репозиториев (CardRepository, TransactionRepository)
│
├── application/                     # Слой Use Cases (оркестрация бизнес-логики)
│   ├── usecase/                     # UseCase-классы (CreateCard..., CreateTransaction..., GetTransactionsByCard...)
│   ├── dto/                         # Application DTO (Command, Response)
│   │   ├── input/                   # Command DTOs (CreateTransactionCommand)
│   │   └── output/                  # Response DTOs (TransactionResponse, PageResponse)
│   └── port/                        # Порты для внешних сервисов
│
├── infrastructure/                  # Адаптеры к внешним системам
│   ├── persistence/                 # JPA-адаптеры (Card..., Transaction...)
│   ├── adapter/                     # ACL-адаптеры (UserQueryAdapter)
│   ├── security/encryption/         # AES-256-GCM шифрование
│   └── config/                      # Инфраструктурная конфигурация
│
├── presentation/                    # HTTP-слой (вход в систему)
│   ├── controller/                  # REST-контроллеры (CardController, TransactionController)
│   ├── dto/request/                 # HTTP Request DTO
│   └── exception/                   # GlobalExceptionHandler
│
├── config/                          # Spring Security, JWT фильтр, OpenAPI
├── controller/                      # Legacy контроллеры (User, Role)
├── service/                         # Legacy сервисы (User, Role, Transaction ❌ удаляется на финальном этапе)
├── entity/                          # Legacy JPA-сущности (User, Role, Transaction ❌ удаляется на финальном этапе)
├── dto/                             # Legacy DTO
├── repository/                      # Legacy JPA-репозитории
├── mapper/                          # Legacy мапперы
├── security/jwt/                    # JWT-сервис
├── exception/                       # Legacy обработчики ошибок
└── util/                            # Утилиты (JwtTokenUtils, GlobalConstants)
```

### Ключевые принципы реализации

- **Rich Domain Model**: `BankCard` содержит всю бизнес-логику (валидация баланса, статусов, сроков действия)
- **Value Objects**: `CardNumber` с валидацией по алгоритму Луна, `ExpiryDate` с проверкой срока, `EncryptedData`
- **Порты и Адаптеры**: Домен не знает о Spring, JPA, шифровании — всё через интерфейсы
- **Защита от IDOR**: Все операции с картами проверяют владельца
- **Anti-Corruption Layer**: `UserQueryAdapter` и `TransactionRecordAdapter` изолируют новую архитектуру от legacy-кода

## ⚙️ Функционал

### 👤 Управление пользователями

**Публичные операции:**
- Регистрация новых пользователей с валидацией данных
- Аутентификация с использованием JWT-токенов

**Операции пользователя:**
- Обновление собственного профиля (имя, email, пароль)

**Административные операции:**
- Управление ролями пользователей (добавление/удаление ролей)
- Просмотр списка всех пользователей с пагинацией и сортировкой
- Получение детальной информации о пользователе по ID
- Удаление пользователей

### 💳 Управление банковскими картами (новая архитектура)

**Операции пользователя:**
- Создание новых карт (с ограничением: макс. 5 карт на пользователя)
- Удаление собственных карт
- Просмотр списка своих карт с пагинацией
- Блокировка и активация собственных карт
- Переводы между своими картами (с проверкой баланса и владельца)

**Административные операции:**
- Просмотр всех карт с фильтрацией по статусу и владельцу
- Модерация статуса карт (активация/блокировка)
- Удаление любых карт из системы

### 💸 Транзакции

**Операции пользователя:**
- Переводы между картами (с записью в историю)
- Просмотр истории операций по конкретной карте с фильтрацией по статусу

**Административные операции:**
- Изменение статуса транзакций
- Просмотр всех транзакций с фильтрацией, пагинацией и сортировкой

### 🔒 Безопасность

- **Шифрование AES-256-GCM**: Номера карт и CVV хранятся в зашифрованном виде
- **Маскирование номеров карт** при отображении (`**** **** **** 1234`)
- **Валидация Luhn** для номеров карт на уровне Value Object
- **Ролевая модель доступа** (USER, ADMIN)
- **Защита от IDOR**: проверка владельца карты в каждом UseCase
- **Security Headers**: HSTS, CSP, X-Frame-Options, Referrer-Policy
- **Docker Hardening**: запуск от непривилегированного пользователя, read-only FS, drop capabilities

## 🏗️ Структура проекта (legacy + новая архитектура)

```
├── config/                # Конфигурация Spring и безопасности
├── controller/            # Legacy REST контроллеры (User, Role)
├── presentation/          # 🆕 Новая HTTP-слой для Card
│   ├── controller/        # CardController, AdminCardController
│   ├── dto/request/       # HTTP Request DTO
│   └── exception/         # GlobalExceptionHandler
├── application/           # 🆕 Слой Use Cases
├── domain/                # 🆕 Доменное ядро
├── infrastructure/        # 🆕 Адаптеры к БД, шифрованию, legacy-модулям
├── dto/                   # Legacy DTO
├── entity/                # Legacy JPA-сущности
├── exception/             # Legacy обработчики исключений
├── mapper/                # Legacy мапперы
├── repository/            # Legacy JPA-репозитории
├── security/              # JWT-компоненты
├── service/               # Legacy сервисы (User, Role, Transaction)
└── util/                  # Утилиты
```

## 👥 Роли пользователей

### USER
- Базовая роль, присваивается при регистрации
- Управление собственным профилем
- Создание и управление своими картами (макс. 5)
- Выполнение переводов между своими картами
- Просмотр истории своих операций

### ADMIN
- Расширенная роль для администрирования системы
- Управление всеми пользователями и ролями
- Модерация карт (блокировка/разблокировка)
- Просмотр и управление всеми транзакциями

## 🚀 Запуск проекта

### Вариант 1: Запуск с использованием Docker (рекомендуется)

1. **Клонируем репозиторий**
   ```bash
   git clone https://github.com/vsvldm/bank-cards-service.git
   cd bank-cards-service
   ```

2. **Создаём файл `.env`** (обязательно, секреты не должны быть в коде)
   ```env
   # Database
   POSTGRES_DB=bank
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=iamroot
   
   # App Database Connection (хост 'db' — имя сервиса в Docker-сети)
   DB_URL=jdbc:postgresql://db:5432/bank
   DB_USERNAME=postgres
   DB_PASSWORD=iamroot
   
   # JWT (Base64, минимум 256 бит)
   JWT_SECRET=vWvk7lsTESTyTincsmZP+34YFmMZKiI/BDA6v5yPSMA=
   
   # Encryption (РОВНО 32 символа для AES-256)
   ENCRYPTION_KEY=12345678901234567890123456789012
   
   # Logging
   SHOW_SQL=false
   
   # CORS
   CORS_ORIGINS=http://localhost:3000
   ```

3. **Собираем проект**
   ```bash
   mvn clean package -DskipTests
   ```

4. **Запускаем с помощью Docker Compose**
   ```bash
   docker-compose up -d --build
   ```

5. **Проверяем статус**
   ```bash
   docker-compose ps
   docker-compose logs app --tail 20
   ```

### Вариант 2: Локальный запуск

1. Клонируем репозиторий
2. Настраиваем переменные окружения (или экспортируем их в shell)
3. Запускаем приложение
   ```bash
   mvn spring-boot:run
   ```

## 🌐 Документация API

После запуска приложения документация API доступна через:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI спецификация**: http://localhost:8080/v3/api-docs
- **Health Check**: http://localhost:8080/actuator/health

## 🧪 Тестирование

```bash
# Запуск всех тестов
mvn test

# Запуск только модульных тестов
mvn test -Dtest=*Test

# Запуск только интеграционных тестов
mvn test -Dtest=*IT
```

## 🐳 Docker Security

Контейнер приложения построен с учётом лучших практик безопасности:

- ✅ Запуск от **непривилегированного пользователя** (`appuser`)
- ✅ **Alpine-based** образ (минимальная поверхность атаки)
- ✅ **Healthcheck** для оркестрации
- ✅ `.dockerignore` исключает `.env`, `.git`, `target/` из образа
- ✅ `read_only: true` и `no-new-privileges` в docker-compose
- ✅ Drop всех Linux capabilities, кроме необходимых

## 📋 Требования к системе

- Java 21 или выше
- Maven 3.6+
- Docker и Docker Compose (для запуска в контейнерах)
- PostgreSQL 14 (при локальном запуске без Docker)

## 🔄 История изменений

### v2.0.0 (2026-08-21) — Миграция на Clean Architecture

**Архитектура:**
- Модуль `Card` полностью переведён на Clean/Hexagonal Architecture
- Выделены слои: `domain`, `application`, `infrastructure`, `presentation`
- Внедрены Value Objects (`CardId`, `CardNumber` с Luhn, `ExpiryDate`, `EncryptedData`, `UserId`)
- Реализованы UseCase-ы: `CreateCard`, `TransferMoney`, `BlockCard`, `ActivateCard`, `DeleteCard`, `GetCards`, `GetAllCards`, `ModerateCardStatus`, `AdminDeleteCard`
- Добавлены порты: `CardRepository`, `EncryptionPort`, `UserQueryPort`, `TransactionRecordPort`
- Реализованы адаптеры: `CardRepositoryAdapter`, `AesEncryptionAdapter`, `UserQueryAdapter`, `TransactionRecordAdapter`

**Безопасность:**
- Защита от IDOR во всех операциях с картами
- Шифрование AES-256-GCM для номеров карт и CVV
- Валидация Luhn для номеров карт
- Ограничение: максимум 5 карт на пользователя

**Docker:**
- Переход на Alpine-based образ
- Запуск от непривилегированного пользователя
- Все секреты вынесены в `.env`
- Healthcheck для оркестрации

**Исправления:**
- Исправлен порядок миграций Liquibase (`bank_cards` создаётся до `transactions`)
- Исправлены типы данных в миграции `transactions` (`bigint` → `uuid`)
- Консолидирована обработка исключений в `GlobalExceptionHandler`