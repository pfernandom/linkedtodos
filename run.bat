cd restli-services
call gradlew build
call gradlew install

start gradlew startServer

cd ..
cd play-framework-gateway
call sbt dist
start sbt run


cd ..
cd ember-ui
call npm install
start ember server

cd..