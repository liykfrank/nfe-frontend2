#!/bin/bash

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Functions.
#-------------------------------------------------------------------------------
function ssh_config_already_patched() {

	local SSHD_CONFIG="$1"

	grep -q "Match Group ${SFTP_USER_GROUP}" "$SSHD_CONFIG"
}

function patch_ssh_config() {

	local SSHD_CONFIG="$1"

	SSHD_CONFIG_TMP="/tmp/sshd_config.tmp"

	sed -e '/Subsystem\s*sftp/d' "$SSHD_CONFIG" > "$SSHD_CONFIG_TMP"

	{
		echo -e "\n# --- Automatically added $(date +%Y%m%d-%H%M%S): start"
		echo -e "\nSubsystem sftp internal-sftp\n"
		echo -e "Match Group ${SFTP_USER_GROUP}"
       		echo -e "\tAllowTCPForwarding no"
        	echo -e "\tX11Forwarding no"
		echo -e "\tForceCommand internal-sftp"
		echo -e "\t# user is chrooted to their home."
		echo -e "\tChrootDirectory %h"
		echo -e "\t# authorized_keys file is stored in a common directory"
		echo -e "\t# with the name of the user's login."
		echo -e "\tAuthorizedKeysFile $SFTP_AUTHORIZED_KEYS_STORE/%u"
		echo -e "\n# --- Automatically added: end\n"

	} >> "$SSHD_CONFIG_TMP"

	# check if generated config is valid
	if ! (sshd -t -f "$SSHD_CONFIG_TMP"); then
	
		echo "Error parsing sshd_config file, result: $SSHD_CONFIG_TMP"
		exit 1
	fi

	# replace config file
	cp -v "$SSHD_CONFIG" /etc/ssh/bck.`date +%Y%m%d-%H%M%S`.sshd_config
	cp -v "$SSHD_CONFIG_TMP" "$SSHD_CONFIG"

	chmod -v 655 "$SSHD_CONFIG"
}

#-------------------------------------------------------------------------------
# Main.
#-------------------------------------------------------------------------------

#-------------------------------------------------------------------------------
# Ask for user's confirmation.
#
# It can be confirmed in line using "-y" as first argument.
#-------------------------------------------------------------------------------
MESSAGE="This script changes the sshd configuration, adds a new\n"
MESSAGE="${MESSAGE}user group and creates the sftp's accounts directory."

confirm_or_exit "$MESSAGE" "$1"

#-------------------------------------------------------------------------------
# sshd_config configuration.
#-------------------------------------------------------------------------------

assert_current_user_is_root

SSHD_CONFIG="/etc/ssh/sshd_config"

if [ ! -e "$SSHD_CONFIG" ]; then

	echo "File $SSHD_CONFIG doesn't exist."
	exit 1
fi

if (ssh_config_already_patched "$SSHD_CONFIG"); then

	echo "$SSHD_CONFIG already configured"
else

	echo "Configuring ${SSHD_CONFIG}..."
	patch_ssh_config "$SSHD_CONFIG"
fi

#-------------------------------------------------------------------------------
# sftp-users group creation.
#-------------------------------------------------------------------------------

create_group_if_doesnt_exist "$SFTP_USER_GROUP"

#-------------------------------------------------------------------------------
# Chroot directory creation: owner should be root.
#-------------------------------------------------------------------------------

mkdir -pv "$SFTP_DIRECTORY"
mkdir -pv "$SFTP_AUTHORIZED_KEYS_STORE"
chgrp -v "$SFTP_USER_GROUP" "$SFTP_AUTHORIZED_KEYS_STORE"
chmod 750 "$SFTP_AUTHORIZED_KEYS_STORE"

