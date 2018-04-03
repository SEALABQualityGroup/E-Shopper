FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-categories-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DCATEGORIES_SERVICE_PORT=5555", "-Xmx64m", "-jar", "/app/microservice-demo-categories-0.0.1-SNAPSHOT.jar"]

EXPOSE 5555
