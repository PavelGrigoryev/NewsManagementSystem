FROM openjdk:17-jdk

ADD user-service/build/libs/user-service-1.0-SNAPSHOT.jar user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]

EXPOSE 8081
