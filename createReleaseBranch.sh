#!/bin/bash

if [ $# != 1 ]; then
  printf "Version number is required.\nUsage: ./release.sh <VERSION>"
  exit 1
fi

git checkout develop
git pull --all --prune
git checkout -b "release/$1"
git tag "$1"
git push -u origin "release/$1" --tags
git checkout develop
