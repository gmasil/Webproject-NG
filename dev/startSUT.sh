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

CONTAINER_NAME=webproject-sut DATA_FILE=webproject-tests/src/test/resources/test-data.yml PUBLIC_ACCESS=false FRONTEND=true ${SOURCE_FOLDER}/dev/startBackendDevContainer.sh
