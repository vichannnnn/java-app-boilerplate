FROM maven:3.8.1-openjdk-11 as build

WORKDIR /app

COPY . .

RUN mvn clean install

EXPOSE 8000

ENTRYPOINT ["java","-jar","./target/app.jar"]