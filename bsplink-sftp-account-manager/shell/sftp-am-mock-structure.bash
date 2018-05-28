#!/bin/bash

#-------------------------------------------------------------------------------
# Mocks the directory structure used by the sftp accounts.
#
# This structure is supposed to be created and updated by a external process,
# but right now that process doesn't exist so the structure is faked.
#
#-------------------------------------------------------------------------------

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Fake data.
#-------------------------------------------------------------------------------
MAIN_GROUP="nfe"
MAIN_ACCOUNTS="057 075"
COUNTRIES="ES GB"
FILE_TYPES="file_type1 file_type2 file_type3"

#-------------------------------------------------------------------------------
# Configuration.
#-------------------------------------------------------------------------------

INBOX_DIRECTORY="inbox"
OUTBOX_DIRECTORY="outbox"
BAD_FILES_DIRECTORY="BAD_FILES"
ELIMINATED_FILES_DIRECTORY="eliminated"

ITEM_MAIN="main"
ITEM_ISOC="isoc"
ITEM_FILE_TYPE="ft"
ITEM_DIRECTORY="dir"

MODE_READ="R"
MODE_READ_WRITE="RW"

TMP_FILE="/tmp/$(basename $0).tmp"

#-------------------------------------------------------------------------------
# Functions.
#-------------------------------------------------------------------------------
function normalize_group_name() {

	echo "$1" | tr "A-Z" "a-z" | tr "_" "-"
}

function get_group_name() {

	local MODE="$1"
	local ITEM="$2"
	local ITEM_ID="$(tr "-" "_" <<<$3)"
	local MODE_CHARACTER="r"

	if [[ "$MODE" == "$MODE_READ_WRITE" ]]; then

		MODE_CHARACTER=w
	fi

	echo "nfe-${MODE_CHARACTER}-${ITEM}-${ITEM_ID}" | tr "A-Z" "a-z"
}

function add_file_types_to_directory() {

	local DIRECTORY="$1"
	local DATA_FILE="$2"

	for FILE_TYPE in $FILE_TYPES; do

		FILE_TYPE_DIRECTORY="$BOX_DIRECTORY/$FILE_TYPE"
		echo "$FILE_TYPE_DIRECTORY|$MODE_READ|$ITEM_FILE_TYPE|${BOX}_$FILE_TYPE" >> $DATA_FILE
		echo "$FILE_TYPE_DIRECTORY|$MODE_READ_WRITE|$ITEM_FILE_TYPE|${BOX}_$FILE_TYPE" >> $DATA_FILE

		if [[ "$BOX" == "$INBOX_DIRECTORY" ]]; then
			BAD_FILES_INBOX_DIRECTORY="$FILE_TYPE_DIRECTORY/$BAD_FILES_DIRECTORY"
			echo "$BAD_FILES_INBOX_DIRECTORY|$MODE_READ|$ITEM_DIRECTORY|$BAD_FILES_DIRECTORY" >> $DATA_FILE
		else

			ELIMINATED_FILES_OUTBOX_DIRECTORY="$FILE_TYPE_DIRECTORY/$ELIMINATED_FILES_DIRECTORY"
			echo "$ELIMINATED_FILES_OUTBOX_DIRECTORY|$MODE_READ_WRITE|$ITEM_DIRECTORY|$ELIMINATED_FILES_DIRECTORY" >> $DATA_FILE
		fi
	done
}

function add_countries_to_directory() {

	local DIRECTORY="$1"
	local DATA_FILE="$2"

	for COUNTRY in $COUNTRIES; do

		COUNTRY_DIRECTORY="$ACCOUNT_DIRECTORY/$COUNTRY"
		echo "$COUNTRY_DIRECTORY|$MODE_READ|$ITEM_ISOC|$COUNTRY" >> $DATA_FILE

		for BOX in $INBOX_DIRECTORY $OUTBOX_DIRECTORY; do

			BOX_DIRECTORY="$COUNTRY_DIRECTORY/$BOX"
			echo "$BOX_DIRECTORY|$MODE_READ|$ITEM_DIRECTORY|$BOX" >> $DATA_FILE

			add_file_types_to_directory "$BOX_DIRECTORY" "$DATA_FILE"
		done
	done
}

function create_data_list() {

	local DIRECTORY="$1"
	local DATA_FILE="$2"

	for ACCOUNT in $MAIN_ACCOUNTS; do

		ACCOUNT_DIRECTORY="$SFTP_ACCOUNTS_DIRECTORY/$ACCOUNT"
		echo "$ACCOUNT_DIRECTORY|$MODE_READ|$ITEM_MAIN|$ACCOUNT" >> $DATA_FILE

		add_countries_to_directory "$ACCOUNT_DIRECTORY" "$DATA_FILE"
	done
}

function create_directories() {

	local DATA_FILE="$1"

	while read LINE; do

		DIRECTORY="$(awk -F\| '{print $1}' <<<$LINE)"
		mkdir -p "$DIRECTORY"

	done <$DATA_FILE
}

function create_groups_and_set_acls() {

	local DATA_FILE="$1"

	create_group_if_doesnt_exist "$MAIN_GROUP"
	chown -R root:$MAIN_GROUP $SFTP_ACCOUNTS_DIRECTORY

	setfacl -R -bknd $SFTP_ACCOUNTS_DIRECTORY

	setfacl -R -m u::--- $SFTP_ACCOUNTS_DIRECTORY
	setfacl -R -m g::--- $SFTP_ACCOUNTS_DIRECTORY
	setfacl -R -m o:--- $SFTP_ACCOUNTS_DIRECTORY

	setfacl -m g:$MAIN_GROUP:rx $SFTP_ACCOUNTS_DIRECTORY

	while read LINE; do

		DIRECTORY="$(awk -F\| '{print $1}' <<<$LINE)"
		MODE="$(awk -F\| '{print $2}' <<<$LINE)"
		ITEM="$(awk -F\| '{print $3}' <<<$LINE)"
		ITEM_ID="$(awk -F\| '{print $4}' <<<$LINE)"

		GROUP="$(get_group_name "$MODE" "$ITEM" "$ITEM_ID")"
		create_group_if_doesnt_exist "$GROUP"

		ACL_MODE="rx"

		if [[ -n "$MODE" && "$MODE" == "$MODE_READ_WRITE" ]]; then

			ACL_MODE="rwx"
		fi

		setfacl -m g:$GROUP:$ACL_MODE $DIRECTORY

	done <$DATA_FILE
}

#-------------------------------------------------------------------------------
# Main.
#-------------------------------------------------------------------------------

> $TMP_FILE
chmod 600 $TMP_FILE

create_data_list "$SFTP_ACCOUNTS_DIRECTORY" "$TMP_FILE"
create_directories "$TMP_FILE"
create_groups_and_set_acls "$TMP_FILE"

rm -f $TMP_FILE
