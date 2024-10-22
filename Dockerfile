FROM openjdk:19-jdk-slim
ARG JAR_FILE=target/todoex*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]