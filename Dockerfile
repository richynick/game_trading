FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/gaming_trading_system-0.0.1-SNAPSHOT.jar gaming_trading_system.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "gaming_trading_system.jar"]

