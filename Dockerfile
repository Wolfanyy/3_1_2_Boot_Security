FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /opt/app
COPY --from=build /opt/app/target/*.jar /opt/app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]