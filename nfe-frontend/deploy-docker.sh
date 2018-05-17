#!/bin/sh

echo
echo "#### STARTING FRONTEND DOCKER DEPLOYMENT ####"

# Defining variables
# -----------------------------------
# Definition of the variables used in this script.
# Currently the values are hardcoded.
# -----------------------------------
EXPOSED_PORT=38080
DOCKER_IMAGE_NAME="nfe/nfe-frontend"
DOCKER_CONTAINER_NAME="nfe-frontend"

# Remove previous docker container and image
# -----------------------------------
# Removes (and stops if needed) previous docker container 
# and the related image.
# -----------------------------------
echo
echo "[INFO] Remove previous docker container and image"
docker rm -f $DOCKER_CONTAINER_NAME
docker rmi $DOCKER_IMAGE_NAME

# Build docker image
# -----------------------------------
# Builds a new docker image from the Dockerfile.
# -----------------------------------
echo
echo "[INFO] Build docker image"
docker build --rm -t $DOCKER_IMAGE_NAME .

# Create and start docker container
# -----------------------------------
# Creates and starts a new docker container starting from 
# previously generated docker image.
# -----------------------------------
echo
echo "[INFO] Create and start docker container"
docker run --name $DOCKER_CONTAINER_NAME -p $EXPOSED_PORT:8080 -d $DOCKER_IMAGE_NAME
