#!/bin/bash
# Capture the certificate from the registry
openssl s_client -showcerts -connect registry-1.docker.io:443 </dev/null 2>/dev/null | openssl x509 -outform PEM > /usr/local/share/ca-certificates/docker-proxy.crt

# Update CA certificates
update-ca-certificates

# Restart Docker
service docker restart

# Verify with hello-world
docker run --rm hello-world
