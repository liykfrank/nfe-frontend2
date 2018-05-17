# nfe/sftp-manager-dev

Docker container with development purposes.

In order to build the image and run a container execute the script
`remove_and_create.sh`. This script will remove any previous image
and container and will create them again.

The build requires of the scripts in `../shell` which are copied
automatically by the script `remove_and_create.sh`.

## Image and container information:

- image name: nfe/sftp-manager-dev
- container name: nfe-sftp-manager-dev
- mapped port: 2222 (host) -> 22 (container)

