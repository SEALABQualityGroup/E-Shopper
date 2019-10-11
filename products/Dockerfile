FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-products-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DPRODUCTS_SERVICE_PORT=4444", "-jar", "/app/microservice-demo-products-0.0.1-SNAPSHOT.jar"]

EXPOSE 4444
