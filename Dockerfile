FROM alpine:latest

RUN  apk --no-cache add openjdk11 git

RUN \
    git clone https://github.com/cl456852/jpetstore-6 && \
    cd jpetstore-6 && \
    ./mvnw clean package

WORKDIR /jpetstore-6

CMD ["./mvnw","cargo:run","-P tomcat90"]

EXPOSE 8080