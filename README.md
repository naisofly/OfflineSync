IBM Cloud Mobile Starter for Offline Data Synch Android

[![](https://img.shields.io/badge/bluemix-powered-blue.svg)](https://bluemix.net)
[![Platform](https://img.shields.io/badge/platform-android-lightgrey.svg?style=flat)](https://developer.android.com/index.html)

### Table of Contents
* [Summary](#summary)
* [Requirements](#requirements)
* [Conversation Configuration](#conversation-configuration)
* [Mobile Foundation Configuration](#mobile-foundation-configuration)
* [Run](#run)
* [License](#license)

### Summary
This IBM Cloud Mobile Offline Data Synch Starter showcases the Mobile Foundation and Offline Data Synch capabilities and provides the integration points for each of the IBM Cloud Mobile services.

### Requirements
* [Android Studio](https://developer.android.com/studio/index.html)
* An [IBM Cloud Account](https://www.bluemix.net/)

Creating an instance of the Offline Data Synch starter will create an instance of Cloudant NoSQL DB and Mobile Foundation service.

### Cloudant Configuration
* To configure cloudant NoSQL DB for the Offline Data Synch app navigate to the cloudant instance created and from the service credentials tab copy the user name and password and update this information in **populate_cloudant_db.sh** which can be found in the project folder. Ensure that you have *execute* and *write* permissions to run these scripts.
* Run **populate_cloudant_db.sh**. This should populate the cloudant database with demo data.


### Mobile Foundation Configuration

##### Steps:
Follow the steps below to configure the Mobile Foundation service

* Open the project in Android Studio and perform a Gradle Sync.

* Go to the project folder and find a the shell script by name **mfpregisterapp.sh** and run it. Ensure that you have *execute* and *write* permissions to run these scripts.

* Deploying the adapter
* Download the JSONStoreSync adapter from the 'Deploying the sync adapter' section from the link "https://mobilefirstplatform.ibmcloud.com/blog/2018/02/23/jsonstoresync-couchdb-databases/"
* After registration , navigate to mobile foundation service created , go to the dashboard , Click on New button beside the Adapters tag, Click on Deploy Adapter button and upload the downloaded .adapter file. 
* Configure the adapter with the Cloudant NoSQL DB credentials with database name as 'foodmenu'.

### Run
You can now run the application on a simulator or a physical device

### License
This package contains code licensed under the Apache License, Version 2.0 (the "License"). You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 and may also view the License in the LICENSE file within this package.
