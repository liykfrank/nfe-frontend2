#!/bin/bash

#-------------------------------------------------------------------------------
# Adds/updates an sftp user.
#-------------------------------------------------------------------------------

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions
#-------------------------------------------------------------------------------

function add_or_update_user() {

	local USER="$1"
	local ROOT_DIRECTORY="$2"
	local USER_GROUPS="$3"

	USER_GROUPS="${SFTP_USER_GROUP},${USER_GROUPS}"
	USER_GROUPS="$(sed -e 's/,$\|\s\+//g' <<<$USER_GROUPS)"

	if (user_exists "$USER"); then

		sudo usermod --home "$ROOT_DIRECTORY" \
			--groups "$USER_GROUPS" \
			"$USER"

		echo "User $USER updated"
	else

		sudo adduser --home "$ROOT_DIRECTORY" \
			--no-create-home \
			--no-user-group \
			--groups "$USER_GROUPS" \
			--shell /sbin/nologin \
			"$USER"

		echo "User $USER added"
	fi
}

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

USER="$1"
ROOT_DIRECTORY="$2"
USER_GROUPS="$3"

if [[ -z "$USER" || -z "$ROOT_DIRECTORY" ]]; then

	echo "Creates or updates an sftp user."
	echo
	echo "$(basename $0) USERNAME ROOT_DIRECTORY \"USER_GROUPS\""
	echo
	echo "ROOT_DIRECTORY"
	echo "	The root directory of the user, it can change in time"
	echo "	depending on changes to the user's given permissions. It must"
	echo "	be a relative path to ${SFTP_ACCOUNTS_DIRECTORY}."
	echo
	echo "USER_GROUPS"
	echo "	Comma separated list of groups to assign to the user."
	echo "	If it is empty all the groups of the user will be removed"
	echo "	(with the exception of the ${SFTP_USER_GROUP} group)."

	exit $USAGE_ERROR
fi

ROOT_DIRECTORY="${SFTP_ACCOUNTS_DIRECTORY}/${ROOT_DIRECTORY}"
ROOT_DIRECTORY="$(sed -e 's%/\+$%%g' <<<$ROOT_DIRECTORY)"

assert_directory_exists "$ROOT_DIRECTORY"
add_or_update_user "$USER" "$ROOT_DIRECTORY" "$USER_GROUPS"

