#!/usr/bin/env bash

cd frontend
yarn test
cd ..

./gradlew test --tests *.*Test
