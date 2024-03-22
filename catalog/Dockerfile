
FROM ubuntu:latest

RUN apt-get update

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk


RUN apt-get install maven -y

WORKDIR /usr

COPY . .

WORKDIR /usr/app
COPY . .


RUN mvn clean package

EXPOSE 4575


CMD ["java","-jar","target/catalog-1.0-SNAPSHOT.jar"]



