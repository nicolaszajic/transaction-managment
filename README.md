# Service Transaction API

This project implements a reactive REST API for managing transactions, built using a **contract-first approach** and following a **hexagonal architecture**.

---

## Tech Stack

* Java 17
* Spring Boot (WebFlux)
* Maven
* OpenAPI Generator
* MapStruct
* Lombok
* Docker

---

## Architecture

The application follows a **Hexagonal Architecture (Ports & Adapters)**:

```
src/main/java/com/mendel/service_transaction
├── domain
├── application
└── infrastructure

src/test/java/com/mendel/service_transaction
├── unit
└── integration
```

### Key principles

* Separation of concerns
* Dependency inversion
* High testability
* Contract-first API design

---

## API Contract

The API is defined using OpenAPI:

```
src/main/resources/contract/service-transaction.yaml
```

The contract is used to generate controllers and models via:

```
openapi-generator-maven-plugin
```
---
## Prerequisites

Before running the project, make sure you have installed:

```
Java 17

Maven 3.9+

Docker

Git
```

Optional but recommended:

```
Eclipse or IntelliJ IDEA

Lombok plugin enabled in the IDE

Annotation processing enabled in the IDE
```
---
## Installation

1. Clone the repository

```
git clone https://github.com/nicolaszajic/transaction-managment.git
cd transaction-managment
```
2. Verify Java and Maven

```
java -version
mvn -version
```
3. Generate OpenAPI sources and compile

```
mvn clean compile
```

This step:

reads the OpenAPI contract

generates controllers and models

runs MapStruct/Lombok annotation processing

compiles the project

4. Run tests

```
mvn test
```

---

## Running the application

### Run locally

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Base URL:

```
http://localhost:8080/
```

---

## Run with Docker

### Build image

```bash
docker build -t service-transaction .
```

### Run container

```bash
docker run -p 8080:8080 service-transaction
```

### Run with profile

```bash
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=local service-transaction
```

---

## Running tests

```bash
mvn test
```

Includes:

* Integration tests using WebFlux
* Parameterized tests (CSV + JSON)
* End-to-end validation of API behavior

---

## Endpoints

### Create Transaction

```
PUT /transactions/{id}
```

Creates a transaction with:

* amount
* type
* optional parent_id

---

### Get Transactions by Type

```
GET /transactions/types/{type}
```

Returns a list of transaction IDs for a given type.

---

### Get Transaction Sum

```
GET /transactions/sum/{id}
```

Returns the sum of all transactions connected by parent_id.

---

## Design Decisions

### 1. Contract-first approach

The API is defined in OpenAPI and used to generate controllers and models, ensuring consistency between contract and implementation.

---

### 2. Hexagonal architecture

Clear separation between:

* Domain logic
* Application use cases
* Infrastructure (controllers, repositories)

---

### 3. Reactive stack (WebFlux)

Chosen for:

* Non-blocking I/O
* Better scalability
* Modern reactive programming model

---

### 4. In-memory repository

Used for simplicity and to focus on business logic.
No persistence layer is included.

---

### 5. BigDecimal for monetary values

Used instead of `double` to avoid precision issues in financial calculations.

---

##  Notes

* Data is stored in-memory (not persistent)
* Application is stateless across restarts

---

## Author

Nicolas Zajic
