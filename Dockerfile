# TODO: build jar from here!
FROM eclipse-temurin:17-jre
#FROM arm64v8/alpine:edge
#FROM arm32v6/openjdk:17
#FROM arm32v6/alpine:3.18.4
#FROM balenalib/raspberry-pi-openjdk:
#FROM ubuntu:22.04

#RUN apk update && \
#    apk upgrade
#RUN apk --no-cache add openjdk17

ARG JAR_FILE

COPY ${JAR_FILE} app.jar

EXPOSE 8080 5432
VOLUME /tmp /configuration /script
ENV PROFILE=prod

ENTRYPOINT ["java","-jar","-Dspring.config.additional-location=file:/configuration/","-Dspring.profiles.active=${PROFILE}","/app.jar"]
