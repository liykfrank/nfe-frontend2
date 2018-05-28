#!/bin/bash

#-------------------------------------------------------------------------------
# Removes the authorized key for a user
#-------------------------------------------------------------------------------

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions
#-------------------------------------------------------------------------------

function delete_key() {

	local USER="$1"

	AUTHORIZED_KEYS_FILE="$(get_authorized_keys_file_for_user "$USER")"
	AUTHORIZED_KEYS_FILE_BCK="${AUTHORIZED_KEYS_FILE}.old"

	if [ -e "$AUTHORIZED_KEYS_FILE" ]; then
		mv -fv "$AUTHORIZED_KEYS_FILE" "$AUTHORIZED_KEYS_FILE_BCK"
	fi
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"

if [[ -z "$USER" ]]; then

	echo "Deletes the user's public key."
	echo
	echo "Usage: $(basename $0) USERNAME"

	exit $USAGE_ERROR
fi

assert_user_exists "$USER"

delete_key "$USER"

echo "Removed public key"
