# Spring AI Heuristic Redactor

An intelligent text redaction and refinement system built with **Spring AI**, **Clean Architecture (Ports and Adapters)**, **Strategy Pattern**, and reactive processing (Project Reactor). The application leverages artificial intelligence to refine, adapt, and structure contextual content, safely persisting the final result into a relational database.

---

## 🚀 What the System Does

1. **AI-Driven Redaction:** Processes raw text and content by applying heuristic guidelines through reactive interactions with AI providers integrated via Spring AI.
2. **Dynamic Contextualization (`toolContext`):** Safely injects crucial request metadata (such as username, title, target category, and chosen model) into the ChatClient execution flow.
3. **Strategy Pattern for Persistence:** Decouples persistence logic using flexible strategies (`SaveRedactorStrategy`) to handle data saving behaviors cleanly.
4. **Safe Enum Handling:** Implements robust and safe conversions (`safeValueOf`) for domain enums (`ModelRedactor` and `ModelTarget`) to prevent failures from invalid or null values.
5. **Integrated AI Tools:** Exposes reactive tools (`@Tool`) allowing the AI to autonomously trigger finalization and persistence flows once the user approves the result.

---

## 🏛️ Project Architecture (Clean Architecture)

The project strictly follows **Ports and Adapters** principles:

* **Domain (`/domain`):** Contains business entities (`Redactor`), enums (`ModelRedactor`, `ModelTarget`), and output ports (`RedactorPort`).
* **Use Cases (`/usecase`):** Orchestrate the main execution flow and integration with the AI client.
* **Infrastructure (`/infrastructure`):**
    * **Adapters & Persistence:** Spring Data JPA repository implementations and output adapters.
    * **Strategy:** Decoupled persistence rules (`SaveRedactorStrategy` and its default implementation).
    * **Tools:** Tools exposed to Spring AI (`RedactorTools`) that handle interoperability and boundary translation with the strategies.

---

## 🛠️ Technologies Used

* **Java 21+**
* **Spring Boot**
* **Spring AI**
* **Spring Data JPA / Hibernate**
* **PostgreSQL / Relational Database**
* **Project Reactor (Flux / Mono)**
* **Lombok**