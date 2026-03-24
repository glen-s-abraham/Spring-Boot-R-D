#!/bin/bash
# SSL Certificate Workaround
echo 'Acquire::https::Verify-Peer "false";' > /etc/apt/apt.conf.d/99verify-peer.conf

# Fix GPG key download
mkdir -p /etc/apt/keyrings
curl -fsSL -k https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
chmod a+r /etc/apt/keyrings/docker.asc

# Update Docker source list
echo "deb [arch=amd64 signed-by=/etc/apt/keyrings/docker.asc trusted=yes] https://download.docker.com/linux/ubuntu noble stable" > /etc/apt/sources.list.d/docker.list

# Update packages and Install Docker
apt-get update
apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Start Docker
service docker start
