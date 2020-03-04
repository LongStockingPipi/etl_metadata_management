FROM  java:8
ADD target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]