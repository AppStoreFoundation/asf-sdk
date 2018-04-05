#!/bin/bash

./gradlew :appcoins-sdk:clean :appcoins-sdk-iab:clean :appcoins-sdk-ads:clean
./gradlew :appcoins-sdk:assemble :appcoins-sdk-iab:assemble :appcoins-sdk-ads:assemble
./gradlew publish bintrayUpload
