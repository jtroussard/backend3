#!/bin/bash

MODE=$1

if [[ "$MODE" == "local-offline" ]]; then
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

elif [[ "$MODE" == "local-cloud" ]]; then
  echo "Starting LOCAL CLOUD mode (Cloud SQL)..."
  echo "Fetching secrets from GCP — ensure you are authenticated with GCP CLI."
  mvn spring-boot:run -Dspring-boot.run.profiles=dev

else
  echo "Usage: ./start-local.sh [local-offline | local-cloud]"
  exit 1
fi
