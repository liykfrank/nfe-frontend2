#!/bin/bash

/usr/sbin/sshd -D &
java -jar bsplink-sftp-account-manager*.jar
