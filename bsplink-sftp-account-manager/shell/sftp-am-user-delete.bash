#!/bin/bash

#-------------------------------------------------------------------------------
# Deletes an sftp user.
#-------------------------------------------------------------------------------

set -e

source sftp-am.source

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"

if [[ -z "$USER" ]]; then

	echo "Deletes an sftp user (doesn't delete the sftp directories)."
	echo
	echo "$(basename $0) USERNAME"
	echo

	exit $USAGE_ERROR
fi

assert_user_exists "$USER"

sftp-am-authorize-key-remove.bash "$USER"

sudo userdel -f "$USER"

echo "User $USER delete"

