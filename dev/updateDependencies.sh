#! /bin/bash
#
# Webproject NG
# Copyright © 2021 - 2022 Gmasil
#
# This file is part of Webproject NG.
#
# Webproject NG is licensed under the Creative Commons
# Attribution-NonCommercial-ShareAlike 4.0 International
# Public License ("Public License").
#
# Webproject NG is non-free software: you can redistribute
# it and/or modify it under the terms of the Public License.
#
# You should have received a copy of the Public License along
# with Webproject NG. If not, see
# https://creativecommons.org/licenses/by-nc-sa/4.0/legalcode.txt
#

set -u

SOURCE_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. && pwd )"

echo "Searching for updates..."
PARENT_LOG=$(mvn -f ${SOURCE_FOLDER}/pom.xml -U -B versions:display-parent-updates --no-transfer-progress -Dmaven.version.rules=https://static.gmasil.de/maven-version-rules.xml)
PROPERTY_LOG=$(mvn -f ${SOURCE_FOLDER}/pom.xml -U -B versions:display-property-updates --no-transfer-progress -Dmaven.version.rules=https://static.gmasil.de/maven-version-rules.xml)

PARENT_UPDATES=$(echo ${PARENT_LOG} | grep -oP '\[INFO\] *\K[-a-z\.:]+ *[0-9\.]+ \-\> [0-9\.]+')
PROPERTY_UPDATES=$(echo ${PROPERTY_LOG} | grep -oP '\[INFO\] \$\{[-a-z\.:]+\} \.+ [^ ]+ \-\> [^ ]+' | sed -r 's/\[INFO\] \$\{(.*?)\}/\1/g')

if [ -z "${PARENT_UPDATES}" ] && [ -z "${PROPERTY_UPDATES}" ]; then
    echo "Everything up to date."
    exit 0
fi

echo "Available updates:"
if [ ! -z "${PARENT_UPDATES}" ]; then
    echo "${PARENT_UPDATES}"
fi
if [ ! -z "${PROPERTY_UPDATES}" ]; then
    echo "${PROPERTY_UPDATES}"
fi

echo "Updating versions..."
LOG=$(mvn versions:update-parent versions:use-latest-versions versions:update-properties --no-transfer-progress -Dmaven.version.rules=https://static.gmasil.de/maven-version-rules.xml -DgenerateBackupPoms=false)

if [ $? -ne 0 ]; then
    echo "An error occurred while updating versions:"
    echo "${LOG}"
    exit 1
fi

echo "Done."
