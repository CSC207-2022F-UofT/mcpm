FROM nginx

# Install dependencies
RUN apt-get update && apt-get install -y \
    certbot \
    python3-certbot-nginx \
    && rm -rf /var/lib/apt/lists/*

# Copy nginx configuration
COPY mcprs.conf /etc/nginx/templates/mcprs.conf.template
