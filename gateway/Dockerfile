FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-gateway-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/microservice-demo-gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 4000
