FROM ubuntu:latest AS build

RUN apt-get update && apt-get install -y openjdk-17-jdk maven

WORKDIR /app
COPY . .

RUN mvn clean install

FROM eclipse-temurin:17-jre-alpine

# Instala fuso horário correto no Alpine
RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone

ENV TZ=America/Sao_Paulo

EXPOSE 3003

COPY --from=build /app/target/Almeidas_Cortes-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Duser.timezone=America/Sao_Paulo", "-jar", "app.jar"]
