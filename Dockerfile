# STAGE 1: Build the application
FROM openjdk:25-slim AS build
WORKDIR /app

# Copy only the maven wrapper and pom.xml first to cache dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Now copy the source code and build the WAR file
COPY src ./src
RUN ./mvnw clean package -DskipTests

# STAGE 2: Run the application
FROM openjdk:25-slim
WORKDIR /app

# Copy only the compiled WAR file from the build stage
# Note: JPetStore usually generates a .war file in the /target folder
COPY --from=build /app/target/*.war ./app.war

# Use a direct java command or a lightweight server instead of running mvnw again
EXPOSE 8080
CMD ["java", "-jar", "app.war"]