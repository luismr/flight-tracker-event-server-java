# Flight Tracker Event Server

![Java](https://img.shields.io/badge/Java-21-blue)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4.x-blue)
![Swagger](https://img.shields.io/badge/Swagger-3.0-orange)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-orange)
![Kafka](https://img.shields.io/badge/Kafka-4.x-red)
![Redis](https://img.shields.io/badge/Redis-7.4-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-green)
![JUnit](https://img.shields.io/badge/JUnit-5.x-blue)
![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.x-blue)

A Spring Boot application for tracking flight events.

## Build Status

[![Java CI with Maven](https://github.com/luismr/flight-tracker-event-server-java/actions/workflows/maven.yml/badge.svg)](https://github.com/luismr/flight-tracker-event-server-java/actions/workflows/maven.yml)
![Coverage](badges/jacoco.svg)
![Branches](badges/branches.svg)

## Features

- Real-time flight position tracking
- Kafka-based event streaming
- PostgreSQL database with read-write routing
- Redis caching
- Swagger/OpenAPI documentation
- Configurable timezone support

## Prerequisites

- Java 17 or later
- Docker and Docker Compose
- PostgreSQL
- Redis
- Kafka

## Configuration

The application can be configured through `application.yml`. Key configurations include:

### Database

```yaml
spring:
  datasource:
    writer:
      jdbcUrl: jdbc:postgresql://localhost:5432/flighttracker
      username: flighttracker
      password: flighttracker
    reader:
      jdbcUrl: jdbc:postgresql://localhost:5433/flighttracker
      username: flighttracker
      password: flighttracker
```

### Kafka

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: flight-tracker-group
    topic:
      flight-positions: flight-positions
      ping-created: ping-created
```

### Redis

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### Clock Configuration

The application uses a configurable clock for timestamp operations. By default, it uses UTC:

```yaml
app:
  clock:
    timezone: UTC
```

You can change the timezone to any valid timezone ID (e.g., "America/New_York", "Europe/London"):

```yaml
app:
  clock:
    timezone: America/New_York
```

### API Documentation

Swagger UI is available at `/swagger-ui.html` with the following configuration:

```yaml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
```

## Development

### Running the Application

1. Start the required services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Testing

Run the tests:
```bash
./mvnw test
```

## API Documentation

The API documentation is available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Getting Started

### External Dependencies

The application requires the following external services:

- Redis 7.4
- PostgreSQL 17
- Apache Kafka 4

#### Using Docker Compose

The project includes a `docker-compose.yml` file that sets up all required services. To manage the services:

```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs for all services
docker-compose logs -f

# View logs for a specific service
docker-compose logs -f redis
docker-compose logs -f postgres
docker-compose logs -f kafka

# Restart a specific service
docker-compose restart redis
docker-compose restart postgres
docker-compose restart kafka

# Stop and remove all containers and volumes
docker-compose down -v
```

#### Service Details

- **Redis**
  - Port: 6379
  - No authentication required
  - Data persistence enabled

- **PostgreSQL**
  - Port: 5432
  - Database: flighttracker
  - Username: flighttracker
  - Password: flighttracker
  - Schema: flighttracker

- **Kafka**
  - Port: 9092
  - Auto topic creation enabled
  - Single broker configuration

#### Manual Service Management

If you prefer to manage services individually:

```bash
# Redis
docker run -d --name redis -p 6379:6379 redis:7.4

# PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_USER=flighttracker \
  -e POSTGRES_PASSWORD=flighttracker \
  -e POSTGRES_DB=flighttracker \
  -p 5432:5432 \
  postgres:17

# Kafka
docker run -d --name kafka \
  -p 9092:9092 \
  -e KAFKA_BROKER_ID=1 \
  -e KAFKA_LISTENERS=PLAINTEXT://:9092 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_AUTO_CREATE_TOPICS_ENABLE=true \
  apache/kafka:4
```

### Cloning the Repository

```bash
git clone git@github.com:luismr/flight-tracker-event-server-java.git
cd flight-tracker-event-server-java
```

### Building

```bash
mvn clean install
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## GitHub Actions Permissions

To enable automatic badge updates and coverage reports, ensure the following GitHub Actions permissions are set:

1. Go to your repository's Settings
2. Navigate to Actions > General
3. Under "Workflow permissions", select:
   - "Read and write permissions"
   - "Allow GitHub Actions to create and approve pull requests"

## Read Write Datasource Routing

![Read Write Deployment](docs/arch_diagram_datasource_read_write.png)  

The application supports read-write splitting for database operations. This feature is disabled by default but can be enabled through configuration.

### Configuration

```yaml
spring:
  datasource:
    writer:
      jdbcUrl: jdbc:postgresql://localhost:5432/flighttracker
      username: flighttracker
      password: flighttracker
      driverClassName: org.postgresql.Driver
      type: com.zaxxer.hikari.HikariDataSource
    reader:
      jdbcUrl: jdbc:postgresql://localhost:5433/flighttracker
      username: flighttracker
      password: flighttracker
      driverClassName: org.postgresql.Driver
      type: com.zaxxer.hikari.HikariDataSource

app:
  read-write-routing:
    enabled: false  # Set to true to enable read-write splitting
```

### Important Notes

1. When enabled, you must configure both write and read data sources
2. The routing is based on Spring's `@Transactional` annotation:
   - Read operations: Use `@Transactional(readOnly = true)`
   - Write operations: Use `@Transactional` or `@Transactional(readOnly = false)`

![Read Write DataSource Routing](docs/arch_diagram_datasource_routing.png)

3. If read-write splitting is enabled but not properly configured, the application will fail to start
4. For development and testing, it's recommended to keep this feature disabled
5. The routing is handled by:
   - `DatasourceConfig`: Configures the data sources and routing
   - `RoutingDataSource`: Routes requests to the appropriate data source
   - `ReadWriteRoutingAspect`: Sets the context based on transaction type
   - `DbContextHolder`: Thread-local holder for the current context

## Message Payloads

The application uses two main DTOs for message communication:

### PingDTO
Used to publish flight position updates to the flight-tracker-event-app via WebSocket.

```json
{
  "id": "uuid",
  "aircraft": {
    "icao24": "string",
    "callsign": "string",
    "origin_country": "string",
    "last_contact": "timestamp",
    "squawk": "string",
    "spi": boolean,
    "sensors": [integer]
  },
  "vector": {
    "velocity": double,
    "true_track": double,
    "vertical_rate": double
  },
  "position": {
    "longitude": double,
    "latitude": double,
    "geo_altitude": double,
    "baro_altitude": double,
    "on_ground": boolean,
    "source": integer,
    "time": "timestamp"
  },
  "last_update": "timestamp"
}
```

### FlightDataDTO
Used to subscribe to flight tracker data from the flight tracker producer via Kafka.

```json
{
  "icao24": "string",
  "callsign": "string",
  "origin_country": "string",
  "last_contact": long,
  "time_position": long,
  "longitude": double,
  "latitude": double,
  "baro_altitude": double,
  "on_ground": boolean,
  "velocity": double,
  "true_track": double,
  "vertical_rate": double,
  "sensors": [integer],
  "geo_altitude": double,
  "squawk": "string",
  "spi": boolean,
  "position_source": integer
}
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── dev/luismachadoreis/flighttracker/server/
│   │       ├── common/           # Common infrastructure and utilities
│   │       ├── flightdata/       # Flight data processing
│   │       └── ping/            # Ping domain and API
│   └── resources/
│       ├── application.yml      # Main configuration
│       └── application-test.yml # Test configuration
└── test/
    └── java/
        └── dev/luismachadoreis/flighttracker/server/
            ├── common/          # Common infrastructure tests
            ├── flightdata/      # Flight data tests
            └── ping/           # Ping domain and API tests
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details. 