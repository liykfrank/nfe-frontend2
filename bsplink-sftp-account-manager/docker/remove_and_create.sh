#!/bin/bash

set -e

IMAGE_NAME="${IMAGE_NAME:-nfe/sftp-manager-dev}"
CONTAINER_NAME="${CONTAINER_NAME:-nfe-sftp-manager-dev}"
HOST_SSH_PORT="${HOST_SSH_PORT:-22222}"
HOST_HTTP_PORT="${HOST_HTTP_PORT:-28080}"

#-------------------------------------------------------------------------------
# Build the service.
#-------------------------------------------------------------------------------

cd ..
./gradlew clean build -x test -x itest
cp build/libs/*jar docker
cd -

#-------------------------------------------------------------------------------
# Script files should be in the build directory in order to docker build to work
#-------------------------------------------------------------------------------

rm -rf ./shell
trap 'rm -rf ./shell' EXIT
cp -rv ../shell/ .

#-------------------------------------------------------------------------------
# Remove and create new image and container.
#-------------------------------------------------------------------------------

if (docker ps -a | grep -q "\<${CONTAINER_NAME}\>"); then

	docker rm -f "$CONTAINER_NAME"
fi

docker build --rm -t "$IMAGE_NAME" .
docker run -d --name "$CONTAINER_NAME" \
	--restart unless-stopped \
	-p $HOST_SSH_PORT:22 \
	-p $HOST_HTTP_PORT:8080 "$IMAGE_NAME"

