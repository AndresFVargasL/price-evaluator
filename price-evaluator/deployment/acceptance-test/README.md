# 🤖 Acceptance Test Project with Karate

This project provides a robust foundation for implementing **integration and acceptance tests** using [Karate](https://github.com/intuit/karate), an open-source framework that unifies **API testing**, **mocks**, **performance testing**, and even **UI automation** under a single DSL, based on the popular **Gherkin BDD syntax**.

Karate enables writing readable, maintainable tests **without requiring step definitions or glue code in Java**, making it ideal for testers and developers alike.

## 🧪 What’s Inside

This project includes:
- Acceptance tests based on the Karate DSL.
- Pre-configured **parallel test execution**.
- Custom utilities for validating responses and OpenAPI schemas.
- HTML test reports for quick feedback.

## 🗂️ Project Structure

```
acceptance-test/
├── src
│   └── test
│       ├── java
│       │   └── com.inditex
│       │       ├── TestParallel.java
│       │       └── utils
│       │           └── ValidatorTestUtils.java
│       └── resources
│           ├── com.inditex
│           │   └── myapp.feature
│           ├── karate-config.js
│           └── logback-test.xml
└── build.gradle
```

- `TestParallel.java`: Executes Karate tests in parallel and generates cucumber-compatible JSON reports.
- `karate-config.js`: Global configuration script where base URLs or custom settings can be injected.
- `prices.feature`: Feature file containing test scenarios based on business requirements.

## 🚀 Running Tests

### Run only acceptance tests (tagged `@acceptanceTest`):
```bash
./gradlew clean test "-Dkarate.options=--tags @acceptanceTest" -i
```

### Run all Karate feature files:
```bash
./gradlew clean test -i
```

## 📊 Reports

After execution, Karate generates detailed HTML reports:

- Path: `build/karate-reports/karate-summary.html`
- Includes summary, execution logs, and assertion results.

---

## 🧠 References

- [Karate GitHub Repository](https://github.com/intuit/karate)
- [Karate Official Docs](https://karatelabs.io/docs/)
- [Karate BDD Syntax Guide](https://karatelabs.io/docs/feature-syntax/)

---

## 👤 Author

Developed with 💥 by **Andrés Vargas** – [@AndresFVargasL](https://github.com/AndresFVargasL)

This project is part of a professional delivery pipeline for validating business rules in a clean and scalable way.

---

> 🧪 *“Testing isn’t just about finding bugs, it’s about building confidence.”*
