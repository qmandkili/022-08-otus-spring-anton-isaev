#
# Build stage
#
FROM maven:3.8.1-adoptopenjdk-11 AS build

ENV PROJECT_DIR=/opt/project

RUN mkdir -p $PROJECT_DIR

ADD ./src $PROJECT_DIR/src
ADD ./pom.xml $PROJECT_DIR

RUN cd $PROJECT_DIR && mvn -f pom.xml package

#
# Package stage
#
FROM openjdk:11-jdk
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=build $PROJECT_DIR/target/homework-17-0.0.1-SNAPSHOT.jar $PROJECT_DIR/
EXPOSE 8080
CMD ["java", "-jar", "/opt/project/homework-17-0.0.1-SNAPSHOT.jar"]