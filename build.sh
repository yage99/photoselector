#!/bin/bash

mvn clean compile assembly:single -P linux
cp target/picselector-*-jar-with-dependencies.jar photoselector.jar
