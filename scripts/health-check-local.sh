#!/bin/bash

HEALTH_URL="http://localhost:8080/actuator/health"

echo "Checking backend health at $HEALTH_URL"

response=$(curl -s -w "\nHTTP_STATUS:%{http_code}\n" "$HEALTH_URL" || echo "Curl failed with exit code $?")

echo "$response"

http_status=$(echo "$response" | grep HTTP_STATUS | cut -d':' -f2)

if [[ "$http_status" != "200" ]]; then
  echo "Health check failed with status: $http_status"
  exit 1
else
  echo "Backend is healthy"
fi
