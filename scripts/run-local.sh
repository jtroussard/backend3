#!/bin/bash

# Fail fast
set -e

# Ensure .env exists
if [ ! -f .env ]; then
  echo ".env file not found."
  exit 1
fi

# Export vars from .env
echo "Loading environment variables..."
export $(grep -v '^#' .env | xargs)

# Run Spring Boot with local profile
echo "Starting backend with 'local' profile..."
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
