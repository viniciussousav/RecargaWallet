# Etapa 1: Build do Maven
FROM maven:3.8.5-openjdk-18 AS build

# Diretório de trabalho para o build
WORKDIR /app

# Copiar o pom.xml e as dependências primeiro (cache eficiente)
COPY pom.xml ./
COPY src ./src

# Executar o build do Maven
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM openjdk:18-jdk-alpine

# Diretório de trabalho no contêiner
WORKDIR /app

# Copiar o JAR gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expor a porta do serviço
EXPOSE 8080

# Comando de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
