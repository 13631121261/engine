FROM openjdk:8
WORKDIR /app
COPY NewSystem-1.1.jar /app
CMD ["java", "-jar", "NewSystem-1.1.jar"]