# Use Java 21 (matching pom.xml)
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies (cached layer)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Run the application
EXPOSE 8090

# Use PORT from Render environment variable, fallback to 8090
ENV PORT=8090
ENTRYPOINT ["sh", "-c", "java -jar target/Hospital-0.0.1-SNAPSHOT.jar --server.port=${PORT} --spring.profiles.active=prod"]

