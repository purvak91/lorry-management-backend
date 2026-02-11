# Lorry Management System – Backend

This is the backend service for the **Lorry Management System**. It provides a robust REST API for managing lorry receipts (LR), including lifecycle management, advanced searching, and real-time autocomplete support.

---

## Quick Start

To start the backend API and its PostgresSQL database:

```bash
docker compose up --build
```

All API endpoints are prefixed with `/api/lorry`.

*The API will be accessible at:* `http://localhost:1001`

---

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Database:** PostgresSQL
- **Migration:** Flyway (Strict version control for schema)
- **Containerization:** Docker & Docker Compose

---

## Features

- **Core CRUD:** Create, update, fetch, and delete Lorry Receipt (LR) records.
- **Advanced Search:** Full-text style search across:
    - LR number
    - Lorry number
    - Consignor name
    - From / To locations
- **Dynamic Filtering:** Server-side pagination, deterministic sorting (LR number descending), and date-range filtering.
- **Autocomplete API:** High-performance endpoints for Lorry numbers, Consignors, and From/To locations.
- **Production Ready:**
    - Graceful shutdown and lifecycle management
    - Custom security headers (XSS protection, frame options)
    - Global exception handling with clear error responses

---

## Environment Variables

The application requires the following variables. When running via **Docker Compose**, these are pre-configured in the `docker-compose.yaml`.

| Variable   | Description                    | Local Example |
| :---       |:-------------------------------| :---          |
| `PORT`     | Port on which the backend runs | `1001`        |
| `JDBC_URL` | PostgresSQL connection string  | `jdbc:postgresql://localhost:5432/lorry_db` |
| `DB_USER`  | Database username              | `postgres`    |
| `DB_PASS`  | Database password              | `your_password` |
| `CORS_ORIGINS` | Allowed origins for CORS       | `http://localhost:3000` |

> [!IMPORTANT]
> **Schema Management:** Hibernate automatic schema generation (`ddl-auto`) is **disabled**. All database changes must be managed via Flyway scripts located in `src/main/resources/db/migration`.

---

## Running with Docker

The `docker-compose.yaml` orchestrates the following:

1. **PostgresSQL:** Database with persistent volume storage.
2. **Flyway:** Automatically handles schema migrations on startup.
3. **Spring Boot App:** The backend service connected to the containerized DB.



### Useful Commands

* **Stop services:** `docker compose down`
* **View logs:** `docker compose logs -f`
* **Reset Database:** `docker compose down -v` (Caution: This removes all stored data)

---

## API Documentation

Once the application is running, you can explore and test the API endpoints using the built-in Swagger UI:

👉 `http://localhost:1001/swagger-ui/index.html`

---

## Health Check

The backend exposes a health endpoint for monitoring:

**GET** `/actuator/health`

A healthy service returns:

```json
{
  "status": "UP"
}

```

---

## Production Notes

- **Schema Integrity:** Database schema changes are managed exclusively via Flyway migrations.
- **DDL Safety:** Hibernate DDL auto-generation is disabled to prevent accidental data loss.
- **Logging:** SQL logging is disabled in production to optimize performance and protect sensitive data.
- **Reliability:** Graceful shutdown is enabled to allow in-flight requests to complete before the service exits.
- **CORS:** Security is enforced by explicitly configuring allowed frontend origins.

---

## Related Repositories

- **Frontend:** https://github.com/purvak91/lorry-management-frontend
