FROM openjdk:8

# Define working directory
WORKDIR /root
EXPOSE 8080

ADD ./server/build/distributions/server-1.0.zip app.zip

RUN unzip app.zip && ls && ls -l ./server-1.0/

CMD ["./server-1.0/bin/server"]