FROM openjdk:8-jdk-alpine 
RUN apk --no-cache add curl
COPY build/libs/*.jar micronaut-aws-lambda-authorizer.jar
CMD java ${JAVA_OPTS} -jar micronaut-aws-lambda-authorizer.jar