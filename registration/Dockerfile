FROM java:8-jre
MAINTAINER sealabaq

COPY target/microservice-demo-registration-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/app/microservice-demo-registration-0.0.1-SNAPSHOT.jar"]
#CMD ["sleep 30"]
#CMD ["/usr/bin/java", "-jar", "/app/microservice-demo-registration-0.0.1-SNAPSHOT.jar"]
EXPOSE 1111
