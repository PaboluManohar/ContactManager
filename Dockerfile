FROM openjdk

COPY target/smc.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

