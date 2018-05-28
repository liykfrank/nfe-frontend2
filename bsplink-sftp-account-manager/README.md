# BSPlink SFTP account manager

Provides a REST API that integrates with the SFTP underlying system to perform different SFTP account management actions: account creation, authorization of public keys, password change, etc...

## Spring profiles

### dev

- activates SQL output
- loads some fake account data used by the fake service [FakeAccountDetailsService](src/main/java/org/iata/bsplink/sftpaccountmanager/service/fake/FakeAccountDetailsService.java).

### test

So as to manage the SFTP accounts the service requires to be executed in a Linux environment or the operations will fail. Activating the `test` profile changes the implementation used to execute the commands by an implementation that will log the commands executed to the console but without actually executing them.

## Docker container for development

There is a [Dockerfile](docker/Dockerfile) that will create a container aimed to be used in the development, to regenerate the image and the container execute:

```sh
cd docker
./remove_and_create.sh
```
ports, image and container names are defined inside the script `remove_and_create.sh`.
