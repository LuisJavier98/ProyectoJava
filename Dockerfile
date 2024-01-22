FROM eclipse-temurin:17-jdk-alpine

VOLUME /tmp

COPY target/*.jar /app/app.jar

EXPOSE 8000

CMD ["java", "-jar", "app.jar"]