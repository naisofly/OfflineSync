#!/bin/sh

CLOUDANT_USER=""
CLOUDANT_PASS=""
CLOUDANT_DB_DOWNSTREAMSYNC="foodmenu"
CLOUDANT_DB_UPSTREAMSYNC="makesales"


echo "Creating databases named ${CLOUDANT_DB_DOWNSTREAMSYNC} and ${CLOUDANT_DB_UPSTREAMSYNC}"

# First, create the databases
curl  https://$CLOUDANT_USER:$CLOUDANT_PASS@$CLOUDANT_USER.cloudant.com/$CLOUDANT_DB_DOWNSTREAMSYNC -X PUT
curl  https://$CLOUDANT_USER:$CLOUDANT_PASS@$CLOUDANT_USER.cloudant.com/$CLOUDANT_DB_UPSTREAMSYNC -X PUT

echo "Adding Data to ${CLOUDANT_DB_DOWNSTREAMSYNC} database" 
#Add data to foodmenu
curl -X POST -H 'Content-type: application/json' -g  https://$CLOUDANT_USER:$CLOUDANT_PASS@$CLOUDANT_USER.cloudant.com/$CLOUDANT_DB_DOWNSTREAMSYNC -d '{"item_name": "Orange Juice",
  "base_price": "3",
  "quantity": "1",
  "image_url": "https://upload.wikimedia.org/wikipedia/commons/6/67/Orange_juice_1_edit1.jpg"}'
