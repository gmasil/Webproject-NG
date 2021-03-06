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

: "${IMAGE_NAME:=webproject-ng}"
: "${IMAGE_TAG:=local-dev}"
: "${LOCAL_PORT:=6900}"
: "${CONTAINER_NAME:=webproject}"
: "${DATA_FILE:=webproject-backend/webproject-data.yml}"
: "${PUBLIC_ACCESS:=true}"
: "${VERBOSE:=false}"
: "${NATIVE:=false}"
: "${BUILD:=true}"
: "${FRONTEND:=false}"
: "${POSTGRES:=false}"
: "${BUILD_LOCAL:=true}"
: "${USER_HOME:=~}"
: "${DOCKER_VERSION:=20.10.9}"

if [ ${NATIVE} == "true" ]; then
    IMAGE_TAG="${IMAGE_TAG}-native"
fi

if [ ${USER_HOME} == "~" ]; then
    USER_HOME="$( cd ~ && pwd )"
fi

echo "IMAGE_NAME     = ${IMAGE_NAME}"
echo "IMAGE_TAG      = ${IMAGE_TAG}"
echo "LOCAL_PORT     = ${LOCAL_PORT}"
echo "CONTAINER_NAME = ${CONTAINER_NAME}"
echo "DATA_FILE      = ${DATA_FILE}"
echo "PUBLIC_ACCESS  = ${PUBLIC_ACCESS}"
echo "VERBOSE        = ${VERBOSE}"
echo "NATIVE         = ${NATIVE}"
echo "BUILD          = ${BUILD}"
echo "FRONTEND       = ${FRONTEND}"
echo "POSTGRES       = ${POSTGRES}"
echo "BUILD_LOCAL    = ${BUILD}"
echo "USER_HOME      = ${USER_HOME}"
echo "DOCKER_VERSION = ${DOCKER_VERSION}"

if [ ${BUILD} == "true" ]; then
    if [ ${NATIVE} == "true" ]; then
        PROFILE=native
    else
        PROFILE=jib
    fi
    FRONTEND_ARGS="-Dnpm.build.skip=true -Dnpm.copy.skip=true"
    if [ ${FRONTEND} == "true" ]; then
        FRONTEND_ARGS=""
    fi
    if [ ${BUILD_LOCAL} == "true" ]; then
        mvn -f ${SOURCE_FOLDER}/pom.xml clean package -pl webproject-backend -am --no-transfer-progress -DskipTests ${FRONTEND_ARGS} -Dtarget.image=${IMAGE_NAME} -Dtarget.tag=${IMAGE_TAG} -P ${PROFILE}
    else
        docker pull registry.gmasil.de/docker/maven-build-container
        MSYS_NO_PATHCONV=1 docker run --rm \
            -v "${SOURCE_FOLDER}:/work:Z" \
            -v "${USER_HOME}/.m2/:/maven/.m2" \
            -v "/var/run/docker.sock:/var/run/docker.sock" \
            -e JAVA_TOOL_OPTIONS='-Duser.home=/maven' \
            registry.gmasil.de/docker/maven-build-container \
            bash -c "
            set -eu
            curl -fsSLO https://download.docker.com/linux/static/stable/x86_64/docker-${DOCKER_VERSION}.tgz
            tar xzvf docker-${DOCKER_VERSION}.tgz --strip 1 -C /usr/local/bin docker/docker
            rm docker-${DOCKER_VERSION}.tgz
            mvn clean package -pl webproject-backend -am --no-transfer-progress -DskipTests ${FRONTEND_ARGS} -Dtarget.image=${IMAGE_NAME} -Dtarget.tag=${IMAGE_TAG} -P ${PROFILE}
            "
    fi
fi

# check if previous container is still running
if docker inspect --format="{{.Id}}" ${CONTAINER_NAME} > /dev/null 2>&1; then
    echo "Stopping previous ${CONTAINER_NAME} container"
    docker stop ${CONTAINER_NAME} > /dev/null 2>&1 || true
    docker rm ${CONTAINER_NAME} > /dev/null 2>&1 || true
fi

IMPORT_ARGS=""
if test -f "${SOURCE_FOLDER}/${DATA_FILE}"; then
    echo "Found ${DATA_FILE}, enabling data import..."
    IMPORT_ARGS="-v ${SOURCE_FOLDER}/${DATA_FILE}:/import.yml:Z -e DATAIMPORT_ENABLED=true -e DATAIMPORT_CLEAN=true -e DATAIMPORT_FILE=/import.yml"
fi

DB_ARGS=()
if [ ${POSTGRES} == "true" ]; then
    DB_ARGS=(--add-host=host.docker.internal:host-gateway -e "SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQL95Dialect" -e "SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/webproject" -e "SPRING_DATASOURCE_USERNAME=webproject" -e "SPRING_DATASOURCE_PASSWORD=webproject")
fi

MSYS_NO_PATHCONV=1 docker run -d --rm --name "${CONTAINER_NAME}" \
    -p ${LOCAL_PORT}:6900 \
    -e "SPRING_PROFILES_ACTIVE=dev" \
    -e "APP_ACCESS_PUBLIC=${PUBLIC_ACCESS}" \
    -e "INIT_THEME_NAME=Webproject Purple" \
    -e "INIT_THEME_BACKGROUNDCOLOR=#5f0066" \
    -e "INIT_THEME_BACKGROUNDHIGHLIGHTCOLOR=#300033" \
    -e "INIT_THEME_PRIMARYCOLOR=#f221ff" \
    -e "INIT_THEME_SECONDARYCOLOR=#b253b8" \
    -e "INIT_THEME_TEXTCOLOR=#f2f2f2" \
    ${IMPORT_ARGS} \
    "${DB_ARGS[@]}" \
    ${IMAGE_NAME}:${IMAGE_TAG}

if [ ${VERBOSE} == "true" ]; then
    docker logs -f "${CONTAINER_NAME}"
fi
