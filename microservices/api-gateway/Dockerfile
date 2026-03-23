FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /home/app/target/*.jar app.jar
EXPOSE 8765
ENTRYPOINT ["java","-jar","/app.jar"]
