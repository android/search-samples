
Android AppSearch Hands-On Session Starter
=======================

Overview
--------

This is the starter code for the DataVerse devices track hands-on session. 
Be sure that you are able to run this application on device prior to the session.
You won't be able to save or search documents, but that will be added during the session. 

If you run into issues running the app, please ping alexsav@ to get a fix quickly.

Features
--------

This sample showcases the following features of AppSearch:

 * Writing a document class with the AppSearch annotation processor
 * Setting an AppSearch database schema
 * Indexing documents into an AppSearch database
 * Searching over indexed documents
 * Using LiveData and ViewModel to retrieve data and display on UI

This is demonstrated through a simple note taking app where the user creates
notes that are indexed into AppSearch, and can then be searched over by
providing a query.

Pre-requisites
--------------

- Android SDK 14

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.
