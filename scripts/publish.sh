#!/bin/bash

cd ..
./gradlew :appcoins-sdk:clean :appcoins-sdk-iab:clean :appcoins-sdk-ads:clean :appcoins-sdk-core:clean
./gradlew :appcoins-sdk:assemble :appcoins-sdk-iab:assemble :appcoins-sdk-ads:assemble :appcoins-sdk-core:assemble
./gradlew publish bintrayUpload
