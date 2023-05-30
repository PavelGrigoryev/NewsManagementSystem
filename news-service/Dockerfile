FROM openjdk:17-jdk

ADD news-service/build/libs/news-service-1.0-SNAPSHOT.jar news-service.jar

ENTRYPOINT ["java", "-jar", "news-service.jar"]

EXPOSE 8080
