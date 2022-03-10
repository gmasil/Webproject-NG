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

set -eu

SOURCE_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. && pwd )"

: "${NETWORK:=}"
: "${USER_HOME:=~}"

if [ ${USER_HOME} == "~" ]; then
    USER_HOME="$( cd ~ && pwd )"
fi

echo "NETWORK        = ${NETWORK}"
echo "USER_HOME      = ${USER_HOME}"
echo ""

ARGS=""
if [ ! -z "${NETWORK}" ]; then
    ARGS="--network ${NETWORK}"
fi

DOCKER_CMD="docker"
UNAME=$(uname -s)
if [[ "${UNAME}" == MINGW* ]] || [[ "${UNAME}" == CYGWIN* ]]; then
    echo "Windows detected: running docker command as winpty"
    DOCKER_CMD="winpty docker"
fi

docker pull registry.gmasil.de/docker/maven-build-container
${DOCKER_CMD} run -ti --rm \
    -v "/${SOURCE_FOLDER}:/work:Z" \
    -v "/${USER_HOME}/.m2/:/maven/.m2:Z" \
    -e "JAVA_TOOL_OPTIONS='-Duser.home=//maven'" \
    ${ARGS} \
    registry.gmasil.de/docker/maven-build-container \
    bash
