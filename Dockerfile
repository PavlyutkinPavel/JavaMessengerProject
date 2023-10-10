FROM openjdk:17-alpine
ARG JAR_FILE=target/MyPerfectMessenger-0.0.1-SNAPSHOT.jar
RUN mkdir /project
WORKDIR /project
COPY ${JAR_FILE} /project
ENTRYPOINT java -jar /project/MyPerfectMessenger-0.0.1-SNAPSHOT.jar
