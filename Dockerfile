# ---- Étape 1: La phase de Build ----
# Utilise une image officielle Maven avec Java 17 pour construire le projet.
# 'eclipse-temurin' est une distribution OpenJDK très populaire et fiable.
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Définit le répertoire de travail à l'intérieur du conteneur
WORKDIR /app

# Copie le fichier de configuration Maven en premier pour utiliser le cache de dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie le reste du code source de l'application
COPY src ./src

# Lance la commande Maven pour packager l'application en JAR
# -DskipTests pour ne pas lancer les tests lors du build sur le serveur
RUN mvn clean package -DskipTests


# ---- Étape 2: La phase d'Exécution ----
# Utilise une image Java beaucoup plus légère, juste pour lancer l'application.
# C'est une bonne pratique pour avoir une image finale plus petite et sécurisée.
FROM eclipse-temurin:17-jre-alpine

# Définit le répertoire de travail
WORKDIR /app

# Copie UNIQUEMENT le fichier JAR créé à l'étape de build
COPY --from=build /app/target/demo-1.0-SNAPSHOT.jar app.jar

# Expose le port sur lequel l'application tourne
EXPOSE 8080

# La commande qui sera exécutée au démarrage du conteneur
ENTRYPOINT ["java", "-jar", "app.jar"]