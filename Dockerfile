FROM openjdk:21-jdk
COPY build/libs/wallet-guardians-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]