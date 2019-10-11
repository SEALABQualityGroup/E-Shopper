FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-order-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DORDER_SERVICE_PORT=5558", "-jar", "/app/microservice-demo-order-0.0.1-SNAPSHOT.jar"]

EXPOSE 5558
