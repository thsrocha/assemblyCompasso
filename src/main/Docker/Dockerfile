FROM frolvlad/alpine-oraclejdk8

ENV SLEEP 3
ENV ENVIRONMENT prod

ADD *.jar /app.jar

RUN sh -c 'touch /app.jar'

EXPOSE 8080

CMD echo "[${ENVIRONMENT}] The application will start in ${SLEEP}s..." && \
    sleep ${SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.profiles.active=${ENVIRONMENT}
