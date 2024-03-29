FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /build
COPY . .
RUN ls -l # This will list the contents of the /build directory
RUN mvn -q clean package -DskipTests

FROM openjdk:21

WORKDIR /app
COPY --from=build /build/btc-scheduler-api/target/*.jar app.jar
ENTRYPOINT ["java","-Xdebug","-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","app.jar"]

EXPOSE 5005