# Build stage
FROM eclipse-temurin:21-jdk as builder


WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/springapi-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
