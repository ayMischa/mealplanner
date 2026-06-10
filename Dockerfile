# --- Build-Stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Maven-Wrapper und pom zuerst kopieren (Layer-Caching)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Quellcode kopieren und bauen
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# --- Runtime-Stage ---
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Nur die fertige JAR aus der Build-Stage holen
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]