#!/bin/bash
cd ..
# Add '.dev' on the version name. Non dev builds are done though jenkins build only
export BUILD_NAME_SUFFIX=".dev"
export BUILD_TYPE_NAME="debug"

./gradlew :appcoins:clean :android-appcoins-billing:clean :appcoins-billing:clean :appcoins-ads:clean :appcoins-core:clean :appcoins-contract-proxy:clean
./gradlew :appcoins:assembleDebug :android-appcoins-billing:assembleDebug :appcoins-billing:assembleDebug :appcoins-ads:assembleDebug :appcoins-core:assembleDebug :appcoins-contract-proxy:assembleDebug
./gradlew publish bintrayUpload