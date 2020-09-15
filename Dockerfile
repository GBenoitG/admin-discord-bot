FROM gradle:6.6.1-jdk8

# copy local dir to app/
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

USER gradle

RUN gradle build --no-daemon

ENTRYPOINT ["gradle"]
