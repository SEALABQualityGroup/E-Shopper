FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-web-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DWEB_SERVICE_PORT=3333", "-Xmx128m", "-jar", "/app/microservice-demo-web-0.0.1-SNAPSHOT.jar"]

EXPOSE 3333
