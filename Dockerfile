FROM openjdk:17
ADD target/transfer-service.jar transfer-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "transfer-service.jar"]