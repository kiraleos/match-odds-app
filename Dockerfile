FROM maven:3.9.9-amazoncorretto-21 AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install -DskipTests

FROM amazoncorretto:21.0.6

WORKDIR /app

RUN ls .
COPY --from=builder /app/target/accepted_demo-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
