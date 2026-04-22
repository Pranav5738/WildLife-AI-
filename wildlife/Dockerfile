# Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

COPY target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Add metadata
LABEL maintainer="wildlife-ai"
LABEL version="1.0"
LABEL description="Wildlife Detection API"

# Create non-root user for security
RUN addgroup -g 1001 appgroup && \
    adduser -D -u 1001 -G appgroup appuser

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder --chown=appuser:appgroup /build/app.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Switch to non-root user
USER appuser

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
