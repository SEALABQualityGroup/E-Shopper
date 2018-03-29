FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-accounts-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DACCOUNT_SERVICE_PORT=6667", "-jar", "/app/microservice-demo-accounts-0.0.1-SNAPSHOT.jar"]
EXPOSE 6667
