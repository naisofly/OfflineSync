#!/bin/sh

########################################################################################
# Note :
# (1) Ensure credentials.xml has populated mobilefoundationUrl & mobilefoundationPassword
# (2) Ensure mobile foundation server (mobilefoundationUrl) is up and running
# (3) In case the package name is inputted while running this script, then the app is
#     created/registered using the inputted package name.
########################################################################################

# Automatically extract the package from AndroidManifest.xml file
package=$(grep "package" $(pwd)/app/src/main/AndroidManifest.xml | awk -F'package=\"' '{print $2}' | awk -F"\"" '{print $1}')

mobilefoundationPassword=$(awk -F '[<>]' '/mobilefoundationPassword/{print $3}' $(pwd)/app/src/main/res/values/credentials.xml)
mobilefoundationUrl=$(awk -F '[<>]' '/mobilefoundationUrl/{print $3}' $(pwd)/app/src/main/res/values/credentials.xml)

# Update mfpclient.properties with the host name
mobilefoundationHost=$(awk -F '[//]' '{print $3}' <<<"$mobilefoundationUrl")
sed -i -e 's/replaceWithHostname/'"${mobilefoundationHost}"'/g' $(pwd)/$(find . -name mfpclient.properties)

OPERATION="Register Application"

registerJson1='{"applicationKey": {"packageName": "'
registerJson2='","version": "1.0","clientPlatform":"android"},"mandatoryScope":"appAuthenticity","securityCheckConfigurations": {"appAuthenticity":{"expirationSec": "1200"}}}'

if [ "$1" == "" ]
then
  registerJson=$registerJson1$(echo $package)$registerJson2
else
  registerJson=$registerJson1$1$registerJson2
fi

HTTP_STATUS=$( curl -s -o /dev/null -w '%{http_code}' -X POST -u admin:$mobilefoundationPassword -H "Content-Type: application/json" -d "$registerJson" "$mobilefoundationUrl/mfpadmin/management-apis/2.0/runtimes/mfp/applications")

if [ "$HTTP_STATUS" == "200" ]
then
    echo "SUCCESS: $OPERATION"
else
  echo "FAILED: $OPERATION"
  echo $HTTP_STATUS
  exit 1
fi