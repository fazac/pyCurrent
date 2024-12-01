FROM openjdk:21-slim
LABEL maintainer=fazac
COPY target/*.jar /cur.jar
ENTRYPOINT ["java","-jar","/cur.jar"]