FROM openjdk:8u162-jre
LABEL maintainer="sealabaq"

COPY target/microservice-demo-wishlist-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java"]
CMD ["-DWISHLIST_SERVICE_PORT=5560", "-jar", "/app/microservice-demo-wishlist-0.0.1-SNAPSHOT.jar"]

EXPOSE 5560
