FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-cart-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DCART_SERVICE_PORT=5559",  "-jar", "/app/microservice-demo-cart-0.0.1-SNAPSHOT.jar"]

EXPOSE 5559
