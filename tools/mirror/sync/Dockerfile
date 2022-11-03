FROM debian:bullseye-slim

# Install dependencies
RUN apt-get update && apt-get install -y \
    rsync \
    python3-schedule \
    && rm -rf /var/lib/apt/lists/*

# Copy clone script
COPY sync.py /app/sync.py

CMD ["python3", "-u", "/app/sync.py"]
