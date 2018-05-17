#!/bin/bash

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions
#-------------------------------------------------------------------------------

function create_or_update_user_directory() {

	local USER="$1"
	local MODE="$2"
	local FILE_MODE

	FILE_MODE="500"

	[[ "$MODE" == "RW" ]] && FILE_MODE="700"

	mkdir -vp $SFTP_DIRECTORY/$USER/$DIR

	chmod -vR "$FILE_MODE" "$SFTP_DIRECTORY/$USER/$DIR"

	chown -v ${USER}:$SFTP_USER_GROUP "$SFTP_DIRECTORY/$USER/$DIR"
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"
MODE="$2"

if [[ -z "$USER" || ! "$MODE" =~ RO|RW ]]; then

	echo "Creates or updates an sftp user."
	echo
	echo "$(basename $0) USERNAME MODE"
	echo
	echo "MODE can be "
	echo "	RO: read-only, the user can only download files"
	echo "	RW: read-write, the user can upload/download files"

	exit 1
fi

assert_user_is_root

if (user_exists "$USER"); then

	echo "User $USER already exists"
else

	echo "Adding user $USER"

	adduser --no-user-group --groups "$SFTP_USER_GROUP" \
		--shell /sbin/nologin "$USER"
fi


USER_DIRS="inbox outbox deleted"

for DIR in $USER_DIRS; do

	create_or_update_user_directory "$USER" "$MODE"
done

