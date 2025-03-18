# Instant Payment API

A simple Spring Boot application which provides RESTful API for transferring money across user accounts.

## Prerequisite

- Maven (v3.9.9)
- Docker (v27.5.1)

## Packaging

The very first thing we need to do is packaging our application into a jar file in order to run it inside a docker container.

`mvn clean package`

## Running the application

Since the application along with the PostgreSQL database and with all the other dependencies (Kafka, Zookeper) are running inside a docker container, we just need to run the following command:

`docker-compose up --build`

## Features

This application has 2 components:

- User accounts - in ACCOUNTS table
- Transactions -  in TRANSACTIONS table

Features available with user accounts:

- Retrieving all user accounts
- Retrieving user account by account id
- Creating new user account

Features available with transactions:

- Retrieving all transactions
- Creating a new transaction

Note: Double transactions and double notifications are handled with PESSIMISTIC_WRITE locks, so nobody can read or write the entities while they are under process.

### Basic API Information
| Method | Path                     | Usage                     |
| --- |--------------------------|---------------------------|
| GET | /v1/accounts             | Retrieve all accounts     |
| GET | /v1/accounts/{accountId} | Retrieve account by id    |
| POST | /v1/accounts             | Create new user account   |
| GET | /v1/transaction          | Retrieve all transactions |
| POST | /v1/transaction          | Create new transaction    |

### Swagger-UI
API Specification is provided in the [swagger-ui page](http://localhost:8080/swagger-ui.html) after spring boot application starts.

Trough this UI we can make some trial of all the available APIs.
Example scenario:
- Creating 2 new user accounts with arbitrary balances
- Listing all the users making sure the users have been created
- Making a money transfer between these 2 accounts
- Querying all the transactions to verify that the transaction was created
- Retrieving user account by id for verifying that the balances are appropriate according to the transaction

Hints:
- GET: In case of listing all entities (accounts or transactions), we just need to hit the "Try it out!" button. When retrieving user account by id, we just need to input the id as path parameter.
- POST: There is a template on Swagger UI that shows how our request body should look like in json format. If we click on that template, it will be copied into the request body area and we just need to update the values.

## Handled exceptions
- Trying to retrieve an account with an id which does not exist
- Trying to make a transfer with more money than we have

## Application shutdown

- `docker-compose down` - for shutting down containers
- `docker system prune -a` - for removing unused images, volumes, and networks

# Improvement ideas
- Beside the unit tests there should be some integration tests as well testing the integration between the app and the database layer (we can for instance use a h2 database for this purpose)
- Maybe we can also have a Notification entity and the corresponding NOTIFICATIONS database table where the Kafka consumer service can save the notification data to be send to the receiver part
- Store the .env file with all the configurations (especially passwords) in a separate, non-public config repository
- Making a multi-stage Dockerfile to containerize Maven Build as well




