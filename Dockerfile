# 1단계: 빌드 단계
FROM gradle:8.4-jdk21 AS builder
WORKDIR /home/app

COPY . .
RUN gradle :finpik-api:clean :api:build -x test

# 2단계: 실행 이미지
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

COPY --from=builder /home/app/api/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
