#!/bin/bash
import subprocess
import re
import os

pwd = os.getcwd()
path = os.path.join(pwd, "..", "gradle", "publish.gradle")

#open up publish.gradle and check the version
file = open(path)

for line in file:
    if "VERSION_CODE" in line:
        match = re.search(r'VERSION_CODE = (\d+)', line)
        if match:
            minor_version_str = match.group(1)

    if "VERSION_NAME" in line:
        match = re.search(r'VERSION_NAME = \"(.+?)\"', line)
        if match:
            prefix_version_str = match.group(1)



# fetch version from maven and see if it is higher to prevent bad versioning
curl_redirect_url = subprocess.check_output(["curl","-w","\"%{redirect_url}\"",
                                  "https://bintray.com/blockchainds/bds/"
                                  "android-appcoins-billing/_latestVersion"])

# parse the version from the end of url
match = re.search(b'/android-appcoins-billing/(.+)\"', curl_redirect_url)
version_in_maven = str(match.group(1).decode("utf-8"))

# make sure that version number was updated
print("Did you update the version number and are confortable doing a release for both"
      " environments (dev and prod)?")
#TODO the b is HAMMERED and we need a better rule to take care of this
local_version = prefix_version_str + minor_version_str + "b"
print("current version is: " + local_version)
print("maven version is: " + version_in_maven)

if local_version == version_in_maven:
    print("aborting because local and maven versions are the same! "
          "Please update local version before doing a new release!")
    exit(-1)

yes = {'yes','y', 'ye', ''}
no = {'no','n'}

choice = input("Type yes to continue or no to abort:\n").lower()
if choice in yes:
    # first publish to dev
    subprocess.call(["sh", "publish_dev.sh"])
    # ./publish_dev.sh

    # now publish to prod
    subprocess.call(["sh", "publish.sh"])

    print("Success! Don't forget to merge commmit your changes to master and tag the new"
          " version there")
elif choice in no:
    print("aborted by user")
    exit(-1)
else:
   sys.stdout.write("Please respond with 'yes' or 'no'")