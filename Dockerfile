FROM openjdk:17-oracle
COPY target/*.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]