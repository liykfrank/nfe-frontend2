#!/bin/bash

set -e

export IMAGE_NAME="nfe/sftp-manager-demo"
export CONTAINER_NAME="nfe-sftp-manager-demo"
export HOST_SSH_PORT="12222"
export HOST_HTTP_PORT="8081"

./remove_and_create.sh
