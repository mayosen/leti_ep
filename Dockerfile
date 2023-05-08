FROM gradle:7.6-jdk17 AS builder
COPY . /home/practice
WORKDIR /home/practice
RUN gradle clean bootJar --no-daemon

FROM openjdk:17-alpine
COPY --from=builder /home/practice/build/libs/*.jar app.jar
COPY tessdata tessdata
ENV LD_LIBRARY_PATH=/usr/local/lib
RUN apk update
RUN apk add --no-cache tesseract-ocr
ENTRYPOINT ["java", "-jar", "app.jar"]
