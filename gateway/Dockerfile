FROM java:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-gateway-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-Xmx64m", "-jar", "/app/microservice-demo-gateway-0.0.1-SNAPSHOT.jar"]

EXPOSE 4000
