FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-cart-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DCART_SERVICE_PORT=5559", "-Xmx64m", "-jar", "/app/microservice-demo-cart-0.0.1-SNAPSHOT.jar"]

EXPOSE 5559
