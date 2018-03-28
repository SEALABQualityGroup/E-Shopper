FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-gateway-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/app/microservice-demo-gateway-0.0.1-SNAPSHOT.jar"]
EXPOSE 4000
