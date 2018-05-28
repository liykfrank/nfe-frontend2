#!/bin/bash

#-------------------------------------------------------------------------------
# Checks the validity of a public key
#-------------------------------------------------------------------------------

set -e

source "$(dirname $0)/sftp-am.source"

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

PUBLIC_KEY="$1"

if [[ -z "$PUBLIC_KEY" ]]; then

	echo "Checks the validity of a public key."
	echo
	echo "Usage: $(basename $0) \"RSA_KEY_STRING\""

	exit $USAGE_ERROR
fi

assert_public_key_is_valid "$PUBLIC_KEY"

echo "Public key is valid"
