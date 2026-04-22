# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy all sources from build context. Supports both:
# 1) context = wildlife/
# 2) context = repo root containing wildlife/
COPY . .

# Build JAR inside Docker so Render doesn't depend on a prebuilt target/ artifact.
RUN set -eux; \
    if [ -f pom.xml ]; then APP_DIR="."; \
    elif [ -f wildlife/pom.xml ]; then APP_DIR="wildlife"; \
    else echo "pom.xml not found in build context"; exit 1; fi; \
    cd "$APP_DIR"; \
    chmod +x mvnw; \
    ./mvnw -DskipTests clean package; \
    cp target/*.jar /build/app.jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Add metadata
LABEL maintainer="wildlife-ai"
LABEL version="1.0"
LABEL description="Wildlife Detection API"

# Install curl for health checks (if wget not available on Render)
RUN apk add --no-cache curl

# Create non-root user for security
RUN addgroup -g 1001 appgroup && \
    adduser -D -u 1001 -G appgroup appuser

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder --chown=appuser:appgroup /build/app.jar app.jar

# Expose port (required for Render)
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

# Switch to non-root user
USER appuser

# JVM options for production
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -XX:+UseStringDeduplication"

# Run application with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
