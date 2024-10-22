#!/bin/bash

rm -rf target/*
mvn clean install -Dmaven.test.skip=true
docker-compose up --build