FROM openjdk:17-alpine
ARG JAR_FILE=target/MyPerfectMessenger-0.0.1-SNAPSHOT.jar
RUN mkdir /c65
WORKDIR /c65
COPY ${JAR_FILE} /c65
ENTRYPOINT java -jar /c65/MyPerfectMessenger-0.0.1-SNAPSHOT.jar
