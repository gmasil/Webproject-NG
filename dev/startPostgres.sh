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

docker run -d --rm \
    -e "POSTGRES_USER=webproject" \
    -e "POSTGRES_PASSWORD=webproject" \
    -e "POSTGRES_DB=webproject" \
    -p "5432:5432" \
    --name webproject-postgres \
    postgres:9.6-alpine
