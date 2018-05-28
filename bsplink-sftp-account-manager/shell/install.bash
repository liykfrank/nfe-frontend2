#!/bin/bash

set -e

BASEDIR="$(dirname $0)"

source "$BASEDIR/sftp-am.source"


INSTALL_DIR="/usr/local/bin"

#-------------------------------------------------------------------------------
# Ask for user's confirmation.
#
# It can be confirmed in line using "-y" as first argument.
#-------------------------------------------------------------------------------

MESSAGE="This script will install the \"Sftp account manager\"\n"
MESSAGE="${MESSAGE}scripts and will change the sshd configuration."

confirm_or_exit "$MESSAGE" "$1"

#-------------------------------------------------------------------------------
# Copy scripts.
#-------------------------------------------------------------------------------

echo "Executing initialization script"
$BASEDIR/$SFTP_AM_INIT_SCRIPT -y

echo "Copying utility scripts"
for SCRIPT in $SFTP_AM_SCRIPTS; do

	cp -vf "$BASEDIR/$SCRIPT" "$INSTALL_DIR"
	chmod -v 555 "$INSTALL_DIR/$SCRIPT"
done
