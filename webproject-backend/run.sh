#! /bin/bash

set -eu

docker run --rm \
  -v /$(pwd)/webproject-data.yml://data.yml \
  -e "DATAIMPORT_ENABLED=true" \
  -e "DATAIMPORT_FILE=//data.yml" \
  -e "APP_ACCESS_PUBLIC=true" \
  -e "SPRING_PROFILES_ACTIVE=dev" \
  -e "INIT_THEME_NAME=Webproject Purple" \
  -e "INIT_THEME_BACKGROUNDCOLOR=#5f0066" \
  -e "INIT_THEME_BACKGROUNDHIGHLIGHTCOLOR=#300033" \
  -e "INIT_THEME_PRIMARYCOLOR=#f221ff" \
  -e "INIT_THEME_SECONDARYCOLOR=#b253b8" \
  -e "INIT_THEME_TEXTCOLOR=#f2f2f2" \
  -p 6900:6900 \
  webproject-backend:0.0.1-SNAPSHOT
