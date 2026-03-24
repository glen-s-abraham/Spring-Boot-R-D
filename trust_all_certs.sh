#!/bin/bash
# Sync time
hwclock -s

# Function to trust a domain's cert
trust_domain() {
    local domain=$1
    local name=$2
    echo "Trusting $domain..."
    openssl s_client -showcerts -connect ${domain}:443 </dev/null 2>/dev/null | openssl x509 -outform PEM > /usr/local/share/ca-certificates/${name}.crt
}

# Domains to trust
trust_domain "registry-1.docker.io" "docker-registry"
trust_domain "auth.docker.io" "docker-auth"
trust_domain "production.cloudflare.docker.com" "docker-cdn"

# Update CA certificates
update-ca-certificates

# Restart Docker
service docker restart

# Verify
docker run --rm hello-world
