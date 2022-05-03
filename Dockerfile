FROM adoptopenjdk/openjdk11:ubi
COPY build/libs/course-enrollment-api-1.0.jar /course-enrollment-api.jar
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar course-enrollment-api.jar" ]
EXPOSE 8080