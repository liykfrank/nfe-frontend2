#!/bin/bash

#-------------------------------------------------------------------------------
# Sets the authorized key for a user
#-------------------------------------------------------------------------------

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions
#-------------------------------------------------------------------------------

function create_or_update_key() {

	local USER="$1"
	local PUBLIC_KEY="$(fix_public_key "$2")"

	AUTHORIZED_KEYS_FILE="$(get_authorized_keys_file_for_user "$USER")"
	AUTHORIZED_KEYS_FILE_BCK="${AUTHORIZED_KEYS_FILE}.old"

	if [ -e "$AUTHORIZED_KEYS_FILE" ]; then
		mv -fv "$AUTHORIZED_KEYS_FILE" "$AUTHORIZED_KEYS_FILE_BCK"
	fi

	echo "$PUBLIC_KEY" > "$AUTHORIZED_KEYS_FILE"

	chgrp -v "$SFTP_USER_GROUP" "$AUTHORIZED_KEYS_FILE"
	chmod -v 440 "$AUTHORIZED_KEYS_FILE"
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"
PUBLIC_KEY="$2"

if [[ -z "$USER" || -z "$PUBLIC_KEY" ]]; then

	echo "Sets the public key for a sftp user."
	echo
	echo "Usage: $(basename $0) USERNAME \"RSA_KEY_STRING\""

	exit $USAGE_ERROR
fi

assert_user_exists "$USER"

assert_public_key_is_valid "$PUBLIC_KEY"
create_or_update_key "$USER" "$PUBLIC_KEY"

echo "Set public key"
