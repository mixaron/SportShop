FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /app

COPY pom.xml ./

# Кэшируем зависимости
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

COPY src ./src

# Пропускаем тесты
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /app

COPY pom.xml ./

RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
