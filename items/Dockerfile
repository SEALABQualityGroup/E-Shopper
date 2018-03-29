FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-items-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DITEMS_SERVICE_PORT=5566", "-jar", "/app/microservice-demo-items-0.0.1-SNAPSHOT.jar"]
EXPOSE 5566
