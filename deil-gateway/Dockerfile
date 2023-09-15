FROM java:8
WORKDIR /opt
ADD ./target/deil-gateway-1.0.0.jar .
CMD ["java","-jar","-Djasypt.encryptor.password=password","deil-gateway-1.0.0.jar"]
EXPOSE 8089