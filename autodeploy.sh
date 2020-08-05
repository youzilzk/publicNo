#!/usr/bin/env bash

cd /nurxat/publicNo &&
ls -l &&
mvn clean &&
ls -l &&
git pull &&
mvn install