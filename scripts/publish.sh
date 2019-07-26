#!/bin/bash
cd ..
# Add '.dev' on the version name. Non dev builds are done though jenkins build only
export BUILD_NAME_SUFFIX=".dev"

./gradlew :appcoins:clean :android-appcoins-billing:clean :appcoins-billing:clean :appcoins-ads:clean
./gradlew :appcoins:assemble :android-appcoins-billing:assemble :appcoins-billing:assemble :appcoins-ads:assemble
./gradlew publish bintrayUpload