FROM eclipse-temurin:23-jdk

WORKDIR /app

# Copy Gradle wrapper and build scripts first to leverage layer caching.
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle.kts settings.gradle.kts ./
RUN chmod +x ./gradlew

# Copy sources and build once to warm caches.
COPY src ./src
RUN ./gradlew --no-daemon build -x test

# Run the bot.
CMD ["./gradlew", "--no-daemon", "run"]
