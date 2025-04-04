# Flight Tracker Event Server

![Java](https://img.shields.io/badge/Java-21-blue)
![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4.x-blue)
![JUnit](https://img.shields.io/badge/JUnit-5.x-blue)
![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.x-blue)
![Kafka](https://img.shields.io/badge/Kafka-4.x-red)
![Redis](https://img.shields.io/badge/Redis-7.4-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-green)

A Spring Boot application for tracking flight events.

## Build Status

[![Java CI with Maven](https://github.com/luismr/flight-tracker-event-server-java/actions/workflows/maven.yml/badge.svg)](https://github.com/luismr/flight-tracker-event-server-java/actions/workflows/maven.yml)
![Coverage](badges/jacoco.svg)
![Branches](badges/branches.svg)

## Getting Started

### Prerequisites

- JDK 21
- Maven 3.8+

### Cloning the Repository

```bash
git clone git@github.com:luismr/flight-tracker-event-server-java.git
cd flight-tracker-event-server-java
```

### Building

```bash
mvn clean install
```

### Running

```bash
mvn spring-boot:run
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

## Project Structure

```
src/
├── main/
│   └── java/
│       └── dev/
│           └── luismachadoreis/
│               └── flighttracker/
│                   └── server/
│                       └── FlightTrackerApplication.java
└── test/
    └── java/
        └── dev/
            └── luismachadoreis/
                └── flighttracker/
                    └── server/
                        └── FlightTrackerApplicationTests.java
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details. 