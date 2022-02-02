#! /bin/bash

set -eu

mvn install -DskipTests
./run.sh
