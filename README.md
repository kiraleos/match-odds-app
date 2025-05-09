# Match & Odds API

This is a Spring Boot RESTful API designed to manage and expose data related to football and basketball matches and
their associated
betting odds. It was developed as part of a take-home interview assignment.

## Features

- Retrieve all matches, including their associated odds.
- Create, update and delete matches.
- Create, update and delete odds for existing matches.
- Data validation to ensure consistency (e.g., no odds without a valid match).
- RESTful endpoints using standard HTTP methods and status codes.
- Fully dockerized.

## Tech Stack

- Java 21
- Spring Boot 3
- Postgres database
- JUnit, Mockito & TestContainers (for unit and functional tests)
- Docker

---

## Design & Implementation Decisions

### 1. Domain Modeling

- **Entities:**
    - `Match`: Represents a match with attributes like `teamA`, `teamB`, `matchDate`.
    - `Odds`: Linked to a `Match` via a `matchId` field. Includes `specifier` and `odd`.

### 2. Error Handling

- Implemented a global exception handler using `@ControllerAdvice` to catch and return meaningful error messages (e.g.,
  404 for not found, 400 for invalid input).
- Validations are enforced with Hibernate Validator (`@NotNull`, `@Min`, etc.) and checked at the service level.

### 3. Persistence

- Postgres is used as the database of choice.
- Spring Data JPA repositories (`MatchRepository`, `OddsRepository`) handle basic CRUD operations.

### 4. Service Layer

- Business logic (e.g., validating match existence before creating odds) is handled in service classes.
- Service layer ensures clear separation of concerns and better testability.

### 5. Testing

- Unit tests cover services with mocked repositories.
- Basic functional (integration) tests cover the controller layer.

---

## API Endpoints

### Match Endpoints

| Method | Endpoint        | Description              |
|--------|-----------------|--------------------------|
| GET    | `/matches`      | List all matches         |
| POST   | `/matches`      | Create a new match       |
| GET    | `/matches/{id}` | Get a single match       |
| PUT    | `/matches/{id}` | Update an existing match |
| DELETE | `/matches/{id}` | Delete an existing match |               

### Odds Endpoints

| Method | Endpoint                       | Description                  |
|--------|--------------------------------|------------------------------|
| GET    | `/matches/{matchId}/odds`      | List all odds for a match    |
| POST   | `/matches/{matchId}/odds`      | Add odds to a match          |
| PUT    | `/matches/{matchId}/odds/{id}` | Update existing odds         |
| GET    | `/matches/{matchId}/odds/{id}` | Get a single odd for a match |
| DELETE | `/matches/{matchId}/odds/{id}` | Delete an existing odd       |

---

## Running the Application

```bash
docker compose up -d --build
```

and optionally

```bash
docker compose logs -f app
```

## Swagger docs

You can access the swagger docs of the API by visiting [this link](http://localhost:1234/swagger-ui/index.html) while
the app is running.

## Comments

1. There should be DTOs instead of returning the entities themselves in the controller layer, but I omitted them for
   this assignment.
2. Factories should be used for creating instances of the entities during testing to avoid setting each field separately
   for every test.