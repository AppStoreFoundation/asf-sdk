#!/bin/bash

cd ..
./gradlew :appcoins:clean :appcoins-iab:clean :appcoins-ads:clean :appcoins-core:clean :microraidenj:clean :microraidenj-bds:clean :ethereumj-android:clean :appcoins-contract-proxy:clean
./gradlew :appcoins:assemble :appcoins-iab:assemble :appcoins-ads:assemble :appcoins-core:assemble :microraidenj:assemble :microraidenj-bds:assemble :ethereumj-android:assemble :appcoins-contract-proxy:assemble
./gradlew publish bintrayUpload
