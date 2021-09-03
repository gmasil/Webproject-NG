#! /bin/bash
#
# Webproject NG
# Copyright © 2021 Gmasil
#
# This file is part of Webproject NG.
#
# Webproject NG is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Webproject NG is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Webproject NG. If not, see <https://www.gnu.org/licenses/>.
#

set -eu

SOURCE_FOLDER="$( cd "$( dirname "${BASH_SOURCE[0]}" )"/.. && pwd )"

: "${IMAGE_NAME:=webproject-ng}"
: "${IMAGE_TAG:=local-dev}"
: "${LOCAL_PORT:=6900}"
: "${CONTAINER_NAME:=webproject}"
: "${DATA_FILE:=webproject-backend/webproject-data.yml}"
: "${VERBOSE:=false}"

mvn -f ${SOURCE_FOLDER}/webproject-backend clean package jib:dockerBuild --no-transfer-progress -DskipCopyFrontend=true -DskipTests -Dtarget.image=${IMAGE_NAME} -Dtarget.tag=${IMAGE_TAG}

# check if previous container is still running
if docker inspect --format="{{.Id}}" ${CONTAINER_NAME} > /dev/null 2>&1; then
    echo "Stopping previous ${CONTAINER_NAME} container"
    docker stop ${CONTAINER_NAME} > /dev/null 2>&1 || true
    docker rm ${CONTAINER_NAME} > /dev/null 2>&1 || true
fi

IMPORT_ARGS=""
if test -f "${SOURCE_FOLDER}/${DATA_FILE}"; then
    echo "Found ${DATA_FILE}, enabling data import..."
    IMPORT_ARGS="-v ${SOURCE_FOLDER}/${DATA_FILE}:/import.yml -e DATAIMPORT_ENABLED=true -e DATAIMPORT_FILE=/import.yml"
fi

MSYS_NO_PATHCONV=1 docker run -d --rm --name "${CONTAINER_NAME}" -p ${LOCAL_PORT}:6900 -e SPRING_PROFILES_ACTIVE=dev ${IMPORT_ARGS} ${IMAGE_NAME}:${IMAGE_TAG}

if [ ${VERBOSE} == "true" ]; then
    docker logs -f "${CONTAINER_NAME}"
fi
