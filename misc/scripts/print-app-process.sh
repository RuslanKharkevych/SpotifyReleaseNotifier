OUTPUT=$(lsof -i :8080)

if [ -n "$OUTPUT" ]; then
  echo "$OUTPUT"
else
  echo "No process found on port 8080"
fi