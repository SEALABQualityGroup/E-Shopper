FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-web-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DWEB_SERVICE_PORT=3333", "-jar", "/app/microservice-demo-web-0.0.1-SNAPSHOT.jar"]

EXPOSE 3333
