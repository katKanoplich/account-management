# Управление Счетом

Это Web-приложение, построенное на Spring Boot, Spring Security, Hibernate и PostgreSQL, позволяющее управлять счетами пользователей.

## Функциональность

Приложение имеет две роли:

1. **Admin(Администратор)**:
    - Может блокировать и разблокировать счета пользователей.
    - Видит список всех существующих счетов.

2. **Client(Владелец счета)**:
    - Может пополнять и снимать деньги со своего счета.
    - Видит информацию только о своем счете.

Если счет заблокирован, то никакие операции с ним не могут быть выполнены.

## Setup

Для запуска приложения требуется наличие следующих компонентов:
- Java Development Kit (JDK) версии 19
- PostgreSQL версии 12
- IntelliJ IDEA или любая другая Java IDE Рекомендуемая версия: IntelliJ IDEA 2021.2
- Spring Boot версии 3.2.8

## Технологии

- Spring Boot
- Spring Security
- Hibernate
- PostgreSQL 

## Запуск приложения
1. Склонируйте репозиторий:

   git clone https://github.com/ваш_username/управление-счетом.git

2. Создайте базу данных PostgreSQL с названием "accountmanagement" и настройте подключение к ней в файле application.yaml

3. Соберите приложение с помощью команды:
   
   ./mvnw clean install

4. Запустите приложение с помощью команды:

  ./mvnw spring-boot:run


Приложение будет запущено на порту http://localhost:8080.

# Коллекция Postman для тестирования API управления учетными записями

Эта коллекция Postman предоставляет набор запросов для тестирования конечных точек REST API контроллеров управления учетными записями.

## Запросы

### GET /api/admin/accounts
Получить список всех учетных записей.

### PUT /api/admin/accounts/{accountId}/block
Заблокировать учетную запись.

### PUT /api/admin/accounts/{accountId}/unblock
Разблокировать учетную запись.

### POST /authentication/register/client
Зарегистрировать нового клиента.

### POST /authentication/register/admin
Зарегистрировать нового администратора.

### POST /authentication/authentication
Аутентифицировать пользователя.

### GET /api/clients/{clientId}/accounts
Получить список счетов клиента.

### POST /api/clients/{clientId}/deposit
Внести депозит на счет клиента.

### POST /api/clients/{clientId}/withdraw
Снять средства со счета клиента.

1. Первоначально зарегистрируем администратора

### POST http://localhost:8080/authentication/register/admin

# В теле запроса разместите следующее
```json
{
"auth":{
    "login": "admin",
    "password": "admin"
}
}
```

2. Зарегистрируем пользователя

### POST http://localhost:8080/authentication/register/client

```json
{
    "firstName":"Kate",
    "lastName":"Kanoplich",
    "age":20,
    "auth":{
      "login":"katkanoplich@mail.com",
      "password":"katkanoplich"
    }
}
```

3. Зарегистрируем второго пользователя

### POST http://localhost:8080/authentication/register/client

```json
{
    "firstName":"Ivan",
    "lastName":"Ivanov",
    "age":20,
    "auth":{
      "login":"ivan@mail.com",
      "password":"ivanivanov"
    }
}
```
4. Ввойдем в аккаунт первого пользователя

### POST http://localhost:8080/authentication/authentication

```json
{
   "login":"katkanoplich@mail.com",
   "password":"katkanoplich"
}
```
# После в теле запроса придет токен, который необходимо скопировать. В Postman во вкладке Authorization выберите Type: Bearer Token, в появившемся окне вставьте скопированный вами ранее токен

5. Пополним счет

### POST http://localhost:8080/api/clients/2/deposit?amount=300

6. Просмотрим счет

### GET  http://localhost:8080/api/clients/2/accounts

7. Снимем со счета деньги

### POST http://localhost:8080/api/clients/2/withdraw?amount=100

8. Зайдем под администратором

### POST http://localhost:8080/authentication/authentication

```json
{
   "login":"admin",
   "password":"admin"
}
```

# После в теле запроса придет токен, который необходимо скопировать. В Postman во вкладке Authorization выберите Type: Bearer Token, в появившемся окне вставьте скопированный вами ранее токен

9. Просмотрим все счета

### GET http://localhost:8080/api/admin/accounts

10. Заблокируем счет первого клиента

### PUT http://localhost:8080/api/admin/accounts/2/block

# Можно повторить действия, представленные для клиента 1 и попробовать положить или снять деньги со счета. Так как он заблокирован, не выйдет ничего сделать.
## Действия для проверки:
1. Ввойдем в аккаунт первого пользователя

### POST http://localhost:8080/authentication/authentication

```json
{
   "login":"katkanoplich@mail.com",
   "password":"katkanoplich"
}
```
# После в теле запроса придет токен, который необходимо скопировать. В Postman во вкладке Authorization выберите Type: Bearer Token, в появившемся окне вставьте скопированный вами ранее токен

2. Пополним счет

### POST http://localhost:8080/api/clients/2/deposit?amount=300

3. Просмотрим счет

### GET  http://localhost:8080/api/clients/2/accounts

4. Снимем со счета деньги

### POST http://localhost:8080/api/clients/2/withdraw?amount=100

# Ввойдем в роли администратора, аналогично методике для входа администратора, представленной выше

# Разблокируем счет клиента 1

### PUT http://localhost:8080//api/admin/accounts/2/unblock

1. Ввойдем в аккаунт второго пользователя

### POST http://localhost:8080/authentication/authentication

```json
{
   "login":"ivan@mail.com",
   "password":"ivanivanov"
}
```
# После в теле запроса придет токен, который необходимо скопировать. В Postman во вкладке Authorization выберите Type: Bearer Token, в появившемся окне вставьте скопированный вами ранее токен

2. Пополним счет

### POST http://localhost:8080/api/clients/3/deposit?amount=300

3. Просмотрим счет

### GET  http://localhost:8080/api/clients/3/accounts

4. Снимем со счета деньги

### POST http://localhost:8080/api/clients/3/withdraw?amount=100













