# Online Banking Microservices API

A modern, scalable online banking system built using a microservices architecture with **Java**, **Spring Boot**, **Apache Kafka**, **PostgreSQL**, **JPA/Hibernate**, **Docker**, and more. This project demonstrates a modular, event-driven banking application with services for account management, transactions, loans, notifications, and authentication.

![Image](https://github.com/user-attachments/assets/29e57f8d-6344-42cd-892b-de8f6235b590)


## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Services](#services)
- [Environment Variables](#environment-variables)
- [Contributing](#contributing)
- [License](#license)

---

## Features
- **Account Management**: Create, update, and manage user accounts and balances.
- **Transaction Processing**: Handle transfers, payments, and transaction history.
- **Loan Management**: Process loan applications, schedules, and credit scoring.
- **Notification System**: Send emails and SMS notifications using event-driven architecture.
- **Authentication**: Secure user authentication with JWT/OAuth2 via Keycloak.
- **Service Discovery**: Dynamic service registration and discovery with Eureka.
- **Distributed Tracing**: Monitor requests with Zipkin.
- **Caching**: Improve performance with Redis.
- **Event-Driven Communication**: Asynchronous messaging with Kafka.

---

## Technologies
- **Backend**: Java, Spring Boot, Spring Cloud
- **Databases**: PostgreSQL (JPA/Hibernate), MongoDB
- **Message Broker**: Apache Kafka, Zookeeper
- **Authentication**: Keycloak (JWT/OAuth2)
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Distributed Tracing**: Zipkin
---
## Prerequisites
- **Docker**: Version 20.10 or higher
- **Docker Compose**: Version 1.29 or higher
- **Java**: JDK 17 (for local development)
- **Maven**: For building Spring Boot services
- **Git**: To clone the repository
- **Containerization**: Docker, Docker Compose
- **Monitoring**: pgAdmin, Mongo Express
- **Email Testing**: Maildev

----
## Setup and Installation
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/online-banking-microservices-api.git
   cd online-banking-microservices-api
   ```
2. **Build the Project (optional, if running locally without Docker):**
  ```bash
 mvn clean install
```
---

```markdown
## Services
| Service            | Description                          | Port       |
|--------------------|--------------------------------------|------------|
| **PostgreSQL**     | Relational database for core data    | 5432       |
| **Keycloak**       | Authentication and authorization     | 9098       |
| **pgAdmin**        | PostgreSQL management UI             | 5050       |
| **Zipkin**         | Distributed tracing                  | 9411       |
| **MongoDB**        | NoSQL database for notifications     | 27017      |
| **Mongo Express**  | MongoDB management UI                | 8081       |
| **Zookeeper**      | Kafka coordination                   | 22181      |
| **Kafka**          | Message broker                       | 9092       |
| **Maildev**        | Email testing server                 | 1080, 1025 |
```
---
## Environment Variables
The following environment variables are defined in `docker-compose.yml`. Customize them as needed:

| Variable                     | Default Value             | Description                     |
|------------------------------|---------------------------|---------------------------------|
| `POSTGRES_USER`              | `postgres`                | PostgreSQL username             |
| `POSTGRES_PASSWORD`          | `admin@123`               | PostgreSQL password             |
| `KEYCLOAK_ADMIN`             | `admin`                   | Keycloak admin username         |
| `KEYCLOAK_ADMIN_PASSWORD`    | `admin`                   | Keycloak admin password         |
| `PGADMIN_DEFAULT_EMAIL`      | `pgadmin4@pgadmin.org`    | pgAdmin login email             |
| `PGADMIN_DEFAULT_PASSWORD`   | `admin`                   | pgAdmin login password          |
| `MONGO_INITDB_ROOT_USERNAME` | `root`                    | MongoDB root username           |
| `MONGO_INITDB_ROOT_PASSWORD` | `password`                | MongoDB root password           |

---

## Contributing
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a Pull Request.


