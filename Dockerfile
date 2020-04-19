FROM adoptopenjdk/openjdk11:jre-11.0.5_10-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT exec java $JAVA_OPTS -cp app:app/lib/* com.microsoft.samples.iot.fiware.publisher.FiwarePublisherApplication