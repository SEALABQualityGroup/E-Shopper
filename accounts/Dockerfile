FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-accounts-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DACCOUNT_SERVICE_PORT=6667", "-Xmx64m", "-jar", "/app/microservice-demo-accounts-0.0.1-SNAPSHOT.jar"]

EXPOSE 6667
