FROM openjdk:19
ARG JAR_FILE=build/libs/Backend-Auth-0.0.1-SNAPSHOT.jar
ARG MONGODB_URI=mongodb://localhost:27017
ARG KAFKA_URL=localhost:9092
ARG APPWRITE_ENDPOINT=http://localhost/v1
ARG APPWRITE_PROJECT_ID=PROJECT_ID
ARG APWRITE_APIKEY=LONG_API_KEY
ENV MONGODB_URI=$MONGODB_URI
ENV APPLICATION_PORT 8082
ENV KAFKA_URL=$KAFKA_URL
ENV APPWRITE_ENDPOINT=$APPWRITE_ENDPOINT
ENV APPWRITE_PROJECT_ID=$APPWRITE_PROJECT_ID
ENV APWRITE_APIKEY=$APWRITE_APIKEY
EXPOSE 8082
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
