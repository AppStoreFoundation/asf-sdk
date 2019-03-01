#!/bin/bash
cd ..
# Add '.dev' on the version name. Non dev builds are done though jenkins build only
export BUILD_NAME_SUFFIX=".dev"

./gradlew :appcoins-ads:clean
./gradlew :appcoins-ads:assemble
./gradlew publish bintrayUpload