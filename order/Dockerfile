
FROM ubuntu:latest

RUN apt-get update

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk


RUN apt-get install maven -y

WORKDIR /usr

COPY . .

WORKDIR /usr/app
COPY . .


RUN mvn package



CMD ["java","-jar","target/order-1.0-SNAPSHOT.jar"]



