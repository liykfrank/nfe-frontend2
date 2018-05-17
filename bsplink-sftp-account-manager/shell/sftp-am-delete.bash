#!/bin/bash

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"

if [[ -z "$USER" ]]; then

	echo "Deletes an sftp user (doesn't delete the sftp directories)."
	echo
	echo "$(basename $0) USERNAME"
	echo

	exit 1
fi

assert_user_is_root

if ! (is_sftp_user "$USER"); then

	echo "User $USER is not an sftp user"

	exit 1
fi

userdel -f -r "$USER"

