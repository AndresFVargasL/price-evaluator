# 💰 price-evaluator

RESTful API for evaluating the correct price of a product based on brand, product ID, and application date.  
Designed with **Clean Architecture**, built with **Spring WebFlux**, and powered by an **in-memory H2 database** for ultra-fast access.

> 🚀 Technical test challenge implemented with precision, scalability, and elegance.

---

## 🧠 Why this project?

In a real-world scenario, price logic must be dynamic, maintainable, and fast.  
This API calculates the correct price considering:
- Product ID
- Brand ID
- Application date  
...and selects the **highest-priority active price** from the database.

---

## 🏗️ Architecture

# Base Project Implementing Clean Architecture

## Before You Begin

We begin by explaining the different project components, starting with the outer layers, continuing with the core business logic (domain), and finally the startup and configuration of the application.

Check out the article [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Architecture

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

This is the innermost module of the architecture. It belongs to the domain layer and encapsulates the business rules and logic through domain models and entities.

## Usecases

This Gradle module also belongs to the domain layer. It implements the use cases of the system, defines application logic, and reacts to invocations from the entry point module, orchestrating the flows toward the domain entities.

## Infrastructure

### Helpers

This section includes general utilities for both Driven Adapters and Entry Points.

These utilities are not tied to specific objects. Instead, they use generics to model common behavior for different persistence objects. These implementations are based on the [Unit of Work and Repository design pattern](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006).

These classes should not exist on their own and must be extended within **Driven Adapters**.

### Driven Adapters

Driven Adapters represent external implementations to our system, such as REST or SOAP connections, database access, file reading, or any other source or destination of data we need to interact with.

### Entry Points

Entry Points represent the points of entry to the application, or the beginning of business flows.

## Application

This is the outermost module of the architecture. It is responsible for assembling the different modules, resolving dependencies, and creating the use case beans (UseCases) automatically by injecting concrete instances of the declared dependencies. It also starts the application (it is the only module with a `public static void main(String[] args)` method).

**The use case beans are automatically made available thanks to a `@ComponentScan` annotation located in this layer.**

---

## 🛠️ Tech Stack

- ☕ **Java 21**
- ⚡ **Spring Boot 3.4.4 (WebFlux)**
- 💡 **Hexagonal Architecture** (Scaffold Clean Architecture plugin)
- 📆 **H2 In-Memory Database**
- 🔍 **Spring Data JPA**
- ✅ **JUnit + WebTestClient + Mockito**
- 🐳 **Docker & Docker Compose**
- 📄 **OpenAPI (Swagger)** included
- 🧪 **Mutation Testing** ready via PITest (optional)

## 📚 API Documentation

After running the app, you can explore the API via Swagger UI:

- 🔗 Swagger UI:  
  [http://localhost:8080/price-evaluator/swagger-ui/index.html](http://localhost:8080/price-evaluator/swagger-ui/index.html)

- 📿 OpenAPI JSON (raw spec):  
  [http://localhost:8080/price-evaluator/v3/api-docs](http://localhost:8080/price-evaluator/v3/api-docs)

---

## ▶️ How to run

### 🧪 With Gradle

```bash
./gradlew clean bootRun --no-daemon
```

Access H2 Console (manually enabled):
```bash
http://localhost:8082/
```
- JDBC URL: `jdbc:h2:mem:mydb`
- User: `sa`
- Password: `pass`

---

### 🐳 With Docker Compose

**PowerShell (Windows):**
```powershell
cd .\price-evaluator\
./gradlew clean build -x test; cd ..; docker-compose up --build
```

**Bash (Linux/macOS):**
```bash
cd ./price-evaluator/
./gradlew clean build -x test && cd .. && docker-compose up --build
```

To stop and remove containers:
```bash
docker-compose down
```

The service will be available at:
```
http://localhost:8080/price-evaluator
```

---

## 🔗 REST Endpoint

### `GET /api/prices`

#### 🛅️ Query Parameters:
| Name             | Type      | Required | Example                    |
|------------------|-----------|----------|----------------------------|
| `applicationDate`| `ISO 8601`| ✅        | `2020-06-14T16:00:00`     |
| `productId`      | `int`     | ✅        | `35455`                   |
| `brandId`        | `int`     | ✅        | `1`                       |

---

### 📤 Example Request:

```http
GET http://localhost:8080/price-evaluator/api/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1
```

### ✅ Example Response:

```json
{
  "productId": 35455,
  "brandId": 1,
  "priceList": 2,
  "startDate": "2020-06-14T15:00:00",
  "endDate": "2020-06-14T18:30:00",
  "price": 25.45,
  "currency": "EUR"
}
```

---

## 🧠 Caching Strategy

To improve response times and reduce database hits, the application uses an in-memory caching mechanism based on **Caffeine**, a high-performance Java caching library.

### 🔧 How it works:
- The method `findApplicablePrice` is annotated with `@Cacheable`.
- Keys are generated based on the combination of `applicationDate`, `productId`, and `brandId`.
- Entries expire 5 minutes after being written and the cache holds a maximum of 100 items.

### 🔍 Configuration Snippet (`application.yaml`):
```yaml
spring:
  cache:
    cache-names: prices
    caffeine:
      spec: maximumSize=100,expireAfterWrite=5m
```

### 📦 Dependencies (`build.gradle`):
```groovy
implementation 'org.springframework.boot:spring-boot-starter-cache'
implementation 'com.github.ben-manes.caffeine:caffeine'
```

---

## 🧪 Test Coverage

To run code and mutation coverage reports:

```bash
./gradlew clean build jacocoMergedReport --no-build-cache --no-daemon
```

> This command must be executed from the root of the `price-evaluator/` project.

This generates the following reports:
- Code coverage report: `price-evaluator/build/reports/jacocoMergedReport/html/index.html`
- Mutation coverage report: `price-evaluator/build/reports/pitest/index.html`

---

## 🪠 Acceptance Tests

Once the project is running, execute the **acceptance tests** from the `price-evaluator/deployment/acceptance-test/` directory:

```bash
./gradlew clean test "-Dkarate.options=--tags @acceptanceTest" -i
```

Report generated:
- `price-evaluator/deployment/acceptance-test/build/karate-reports/karate-summary.html`

---

## 📁 Project Structure

```
price-evaluator/
├── domain/                          → Domain entities and models
├── usecase/                         → Use case logic and application rules
├── applications/
│   └── app-service/                 → Main Spring Boot app with startup config
├── infrastructure/
│   ├── entry-points/
│   │   └── reactive-web/            → RouterFunction and handlers
│   └── driven-adapters/
│       └── jpa-repository/          → Spring JPA integration and persistence logic
├── deployment/
│   ├── Dockerfile                   → Dockerfile for containerization
│   └── acceptance-test/             → Karate feature files and runner
README.md                            → You're reading it 😍
compose.yaml                         → Docker orchestration file
```

---

## ⚖️ License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

Developed with care by **Andrés [@AndresFVargasL](https://github.com/AndresFVargasL)**

---

> 💬 *“Elegance is not a dispensable luxury but a factor that decides between success and failure.”*  
— Edsger W. Dijkstra
