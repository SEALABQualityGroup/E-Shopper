FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-categories-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DCATEGORIES_SERVICE_PORT=5555", "-jar", "/app/microservice-demo-categories-0.0.1-SNAPSHOT.jar"]

EXPOSE 5555
