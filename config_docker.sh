#!/bin/bash
# Configure insecure registries
echo '{"insecure-registries": ["docker.io", "registry-1.docker.io"]}' > /etc/docker/daemon.json

# Restart Docker
service docker restart

# Verify with hello-world
docker run --rm hello-world
