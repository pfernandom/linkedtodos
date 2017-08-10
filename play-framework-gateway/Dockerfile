FROM openjdk:8

# Define working directory
WORKDIR /root
EXPOSE 9000

ENV restHost "http://api:8080/" 

ADD ./target/universal/front-1.0-SNAPSHOT.zip app.zip

RUN unzip app.zip && ls && ls -l ./front-1.0-SNAPSHOT/

CMD ["./front-1.0-SNAPSHOT/bin/front", "-Dplay.http.secret.key='mysecretshhh'"]