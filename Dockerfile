FROM eclipse-temurin:22-jdk AS buildstage 

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
COPY src /app/src
COPY Wallet_WMTRTPPFEQWRRW3C /app/Wallet_WMTRTPPFEQWRRW3C

ENV TNS_ADMIN=/app/wallet

RUN mvn clean package -DskipTests=true

FROM eclipse-temurin:21-jdk 

COPY --from=buildstage /app/target/main-0.0.1-SNAPSHOT.jar /app/app.jar

COPY Wallet_WMTRTPPFEQWRRW3C /app/Wallet_WMTRTPPFEQWRRW3C
ENTRYPOINT [ "java", "-jar","/app/app.jar" ]