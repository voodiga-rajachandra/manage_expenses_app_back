FROM openjdk:21
COPY /target/FirstPetProject-0.0.1-SNAPSHOT.jar FirstPetProject-0.0.1-SNAPSHOT.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "FirstPetProject-0.0.1-SNAPSHOT.jar"]
