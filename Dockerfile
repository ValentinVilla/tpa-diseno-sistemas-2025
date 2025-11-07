FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*-jar-with-dependencies.jar /app/app.jar

ENV PORT=7000
EXPOSE 7000

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]