FROM java:8
MAINTAINER Deil<deil12@163.com>
WORKDIR /opt

#ADD ./sentinel-1.8.6.jar .
#EXPOSE 8858
#ENTRYPOINT ["java", "-jar", "sentinel-1.8.6.jar"]

#COPY ./sentinel-1.8.6.jar /sentinel-1.8.6.jar
#EXPOSE 8858
#ENTRYPOINT ["java", "-jar", "app.jar"]

ADD ./deil-gateway-1.0.0.jar .
EXPOSE 8089
ENTRYPOINT ["java","-jar","-Djasypt.encryptor.password=password","deil-gateway-1.0.0.jar"]
