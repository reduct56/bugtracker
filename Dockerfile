FROM maven:3.9-eclipse-temurin-21 as DEPENDENCIES
WORKDIR /app
COPY backend/pom.xml backend/pom.xml
COPY shared/pom.xml shared/pom.xml
COPY frontend_app/pom.xml frontend_app/pom.xml
COPY pom.xml .
RUN mvn -B -e -pl backend,shared org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

FROM maven:3.9-eclipse-temurin-21 as BUILDER
WORKDIR /app
COPY --from=DEPENDENCIES /root/.m2 /root/.m2
COPY --from=DEPENDENCIES /app /app
COPY backend/src /app/backend/src
COPY shared/src /app/shared/src
RUN mvn -B -e -pl backend,shared clean install -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=BUILDER /app/backend/target/backend-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]

