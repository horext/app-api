# syntax=docker/dockerfile:experimental
FROM ghcr.io/graalvm/jdk:22.3.2 AS build

RUN microdnf install -y findutils

WORKDIR /usr/src/app

COPY . .
# Update package lists and Install Gradle
RUN chmod +x ./gradlew
RUN ./gradlew nativeCompile

FROM debian:bookworm-slim

WORKDIR /app
RUN addgroup -S demo && adduser -S demo -G demo
USER demo
# Copy the native binary from the build stage
COPY --from=build /usr/src/app/target/api /app/api

# Run the application
CMD ["/app/api"]