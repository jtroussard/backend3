#!/bin/bash

echo "Starting LOCAL OFFLINE mode (local DB)..."
if [[ -f ".env" ]]; then
  set -a
  source "$(dirname "$0")/../.env"
  set +a
else
  echo "Missing .env file for local DB. Exiting."
  exit 1
fi
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
