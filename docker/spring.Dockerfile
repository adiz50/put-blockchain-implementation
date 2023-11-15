FROM maven:3.8.6-openjdk-18

COPY backend/ /backend
COPY certs/localhost.jks /backend/src/main/resources/localhost.jks
WORKDIR /backend

RUN mvn clean install -DskipTests -P docker -e

CMD ["java","-jar","/backend/target/backend-1.0.0.jar"]
