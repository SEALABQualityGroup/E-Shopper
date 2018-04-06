FROM openjdk:8-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-wishlist-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-DWISHLIST_SERVICE_PORT=5560", "-Xmx64m", "-jar", "/app/microservice-demo-wishlist-0.0.1-SNAPSHOT.jar"]

EXPOSE 5560
