FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-order-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DORDER_SERVICE_PORT=5558", "-Xmx64m", "-jar", "/app/microservice-demo-order-0.0.1-SNAPSHOT.jar"]

EXPOSE 5558
