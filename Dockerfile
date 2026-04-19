FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./
RUN chmod +x mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests package \
    && JAR_FILE=$(find target -maxdepth 1 -name "*.jar" ! -name "*.jar.original" | head -n 1) \
    && cp "$JAR_FILE" target/app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/app.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} -jar /app/app.jar"]
