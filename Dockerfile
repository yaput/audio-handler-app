# Stage 1: Build
FROM gradle:8.10.1-jdk22 AS build
WORKDIR /app
COPY . /app
RUN gradle :audio-service-application:bootJar --no-daemon


WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "audio-service-application/build/libs/audio-service-application-0.0.1.jar"]