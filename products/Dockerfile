FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-products-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DPRODUCTS_SERVICE_PORT=4444", "-jar", "/app/microservice-demo-products-0.0.1-SNAPSHOT.jar"]

EXPOSE 4444
