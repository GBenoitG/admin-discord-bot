FROM openjdk:8-jdk-alpine

# copy local dir to app/
COPY . /app
WORKDIR app

# build project with gradlew
RUN ./gradlew assembleDist
RUN ./gradlew run --args="init"

CMD ./gradlew run