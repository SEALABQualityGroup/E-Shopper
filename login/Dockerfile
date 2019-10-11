FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-login-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DLOGIN_SERVICE_PORT=5557", "-jar", "/app/microservice-demo-login-0.0.1-SNAPSHOT.jar"]

EXPOSE 5557
