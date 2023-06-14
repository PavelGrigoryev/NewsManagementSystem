FROM openjdk:17-jdk

ADD spring-cloud-config/build/libs/spring-cloud-config-1.0-SNAPSHOT.jar spring-cloud-config.jar

ENTRYPOINT ["java", "-jar", "spring-cloud-config.jar"]

EXPOSE 8888
