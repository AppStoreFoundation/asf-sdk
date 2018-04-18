#!/bin/bash

cd ..
./gradlew :appcoins:clean :appcoins-iab:clean :appcoins-ads:clean :appcoins-core:clean
./gradlew :appcoins:assemble :appcoins-iab:assemble :appcoins-ads:assemble :appcoins-core:assemble
./gradlew publish bintrayUpload
