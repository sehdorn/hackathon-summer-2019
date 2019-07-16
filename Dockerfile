FROM alpine:latest

RUN apk add curl

RUN curl -L -o /tmp/graalvm.tar.gz https://github.com/oracle/graal/releases/download/vm-19.1.0/graalvm-ce-linux-amd64-19.1.0.tar.gz

RUN tar -xvf /tmp/graalvm.tar.gz