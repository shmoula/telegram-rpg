# Stage 1: Build
FROM eclipse-temurin:23-jdk AS builder
WORKDIR /app
COPY . .
RUN ./gradlew --no-daemon shadowJar

# Stage 2: Runtime
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
# Copy the specific JAR we just built
COPY --from=builder /app/build/libs/telegram-rpg.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseSerialGC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]