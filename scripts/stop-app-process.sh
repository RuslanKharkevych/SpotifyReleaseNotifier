PID=$(lsof -ti :8080)

if [ -n "$PID" ]; then
  echo "Killing process with PID: $PID"
  sudo kill -9 "$PID"
  echo "Process $PID terminated"
else
  echo "No process found on port 8080"
fi