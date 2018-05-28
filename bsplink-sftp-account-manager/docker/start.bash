#!/bin/bash

set -e

source sftp-am.source

MOCK_STRUCTURE_DONE_FILE="/sftp-am-mock-structure.done"

if ! [ -d "$SFTP_ACCOUNTS_DIRECTORY" ]; then

	sftp-am-mock-structure.bash
fi

/usr/sbin/sshd -D &
java -jar bsplink-sftp-account-manager*.jar
