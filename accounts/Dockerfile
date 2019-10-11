FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-accounts-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DACCOUNT_SERVICE_PORT=6667", "-jar", "/app/microservice-demo-accounts-0.0.1-SNAPSHOT.jar"]

EXPOSE 6667
