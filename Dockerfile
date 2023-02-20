# Use a base image with Java and NodeJS installed
FROM eclipse-temurin:18 as build

# Set the working directory for the app
WORKDIR /app

# Move to the frontend directory
WORKDIR /app/frontend

# Copy the frontend source code to the container
COPY ./cogito-front ./

# Install Node.js and npm
RUN curl -sL https://deb.nodesource.com/setup_14.x | bash - && \
    apt-get update && apt-get install -y nodejs

# Install dependencies and build the Angular app
RUN npm ci && npm run build

# Move back to the root directory
WORKDIR /app

# Copy the built Angular app to the backend resources directory
RUN mkdir -p ./backend/src/main/resources/static/
RUN cp -R ./frontend/dist/* ./backend/src/main/resources/static/

WORKDIR /app/backend

# Copy the backend source code to the container
COPY ./cogito-back ./

# Build the backend application
RUN ./mvnw clean install


# Use a new base image for the final app
FROM eclipse-temurin:18

# Set the working directory for the app
WORKDIR /app

# Copy the built backend JAR to the container
COPY --from=build /app/backend/target/*.jar ./

RUN mkdir -p ./image
RUN mkdir -p ./db/cogito

# Expose the port used by the app
EXPOSE 9191

# Start the app
CMD ["java", "-jar", "cogito-1.0.0.jar"]
