FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/Tic-Tac-Toe-1.0.jar app.jar
EXPOSE 6666
ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
