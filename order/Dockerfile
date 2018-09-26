FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-order-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DORDER_SERVICE_PORT=5558", "-jar", "/app/microservice-demo-order-0.0.1-SNAPSHOT.jar"]

EXPOSE 5558
