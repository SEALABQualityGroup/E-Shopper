FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-config-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD [ "-jar", "/app/microservice-demo-config-0.0.1-SNAPSHOT.jar"]

EXPOSE 8888
