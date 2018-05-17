#!/bin/bash

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions
#-------------------------------------------------------------------------------

function is_valid_public_key() {

	local PUBLIC_KEY="$1"
	local TEMP_KEY="$(mktemp)"

	echo "$PUBLIC_KEY" > "$TEMP_KEY"

	ssh-keygen -l -f "$TEMP_KEY"

	IS_VALID=$?

	rm $TEMP_KEY

	return $IS_VALID
}

function create_or_update_key() {

	local USER="$1"
	local PUBLIC_KEY="$2"

	SSH_DIRECTORY="$SFTP_USERS_HOME/$USER/.ssh"
	AUTHORIZED_KEYS_FILE="$SSH_DIRECTORY/authorized_keys"
	AUTHORIZED_KEYS_FILE_BCK="$SSH_DIRECTORY/authorized_keys.old"

	mkdir -vp "$SSH_DIRECTORY"
	chmod -v 700 "$SSH_DIRECTORY"

	if [ -e "$AUTHORIZED_KEYS_FILE" ]; then
		mv "$AUTHORIZED_KEYS_FILE" "$AUTHORIZED_KEYS_FILE_BCK"	
	fi

	echo "$PUBLIC_KEY" > "$AUTHORIZED_KEYS_FILE"

	chown -vR ${USER}:$DEFAULT_USER_GROUP "$SSH_DIRECTORY"
	chmod -v 644 "$AUTHORIZED_KEYS_FILE"
}

function delete_extra_quotes() {

	# Workaround for the "Public key is not valid" error caused
	# by the addition of extraquotes to the public key.

	echo "$1" | sed -e 's/"//g'
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"
PUBLIC_KEY="$(delete_extra_quotes "$2")"

if [[ -z "$USER" || -z "$PUBLIC_KEY" ]]; then

	echo "Sets the public key for a sftp user."
	echo
	echo "$(basename $0) USERNAME \"RSA_KEY_STRING\""

	exit 1
fi

assert_user_is_root

if ! (user_exists "$USER"); then

	echo "User $USER doesn't exist"
	exit 1
fi

if ! (is_valid_public_key "$PUBLIC_KEY"); then

	echo "Public key is not valid"
	exit 1
fi

create_or_update_key "$USER" "$PUBLIC_KEY"
