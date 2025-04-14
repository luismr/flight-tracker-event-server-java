# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Build arguments for GitHub authentication
ARG SPRING_COMMANDER_USERNAME
ARG SPRING_COMMANDER_TOKEN
ARG SPRING_DATASOURCE_ROUTING_USERNAME
ARG SPRING_DATASOURCE_ROUTING_TOKEN

WORKDIR /app

COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests --settings .mvn/settings.xml

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/flight-tracker-event-server-*.jar ./app.jar

# Create a script to run the application
RUN echo '#!/bin/sh\njava -jar /app/app.jar' > /app/entrypoint.sh && \
    chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"] 