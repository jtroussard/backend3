#!/bin/bash

API_URL="http://localhost:8080/api/user/register"

# Sample test user payload (adjust as needed)
read -r -d '' PAYLOAD << EOF
{
  "username": "testuser123",
  "email": "testuser@example.com",
  "password": "Password123!",
  "confirmPassword": "Password123!"
}
EOF

echo "Registering user against $API_URL"

# Perform the registration request
response=$(curl -s -w "\nHTTP_STATUS:%{http_code}\n" -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")

# Extract HTTP status code
http_status=$(echo "$response" | grep HTTP_STATUS | cut -d':' -f2)
body=$(echo "$response" | sed '/HTTP_STATUS/d')

# Output response
echo "Response Body:"
echo "$body"
echo "HTTP Status: $http_status"

# Validate success
if [[ "$http_status" == "201" ]]; then
  # Optionally, check for the presence of userId in the response
  user_id=$(echo "$body" | grep -o '"userId":[0-9]*' | cut -d':' -f2)
  if [[ -n "$user_id" ]]; then
    echo "Registration successful. User ID: $user_id"
    exit 0
  else
    echo "Registration failed: 'userId' not found in response."
    exit 1
  fi
else
  echo "Registration failed with status: $http_status"
  exit 1
fi
