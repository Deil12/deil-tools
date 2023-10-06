FROM java:8
WORKDIR /opt
ADD ./target/deil-quartz-1.0.0.jar .
CMD ["java","-jar","-Djasypt.encryptor.password=password","deil-quartz-1.0.0.jar"]
EXPOSE 8091