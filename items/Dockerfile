FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-items-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DITEMS_SERVICE_PORT=5566", "-jar", "/app/microservice-demo-items-0.0.1-SNAPSHOT.jar"]

EXPOSE 5566
