# Massive refund loader examples

This folder contains examples aimed to do manual tests with the massive refund loader.

There are two files prepared to do a manual test with the loader:

  - `files/AAe9EARS_20170410_1234_001`
  - `files/AAe9EARS_20170410_1234_002`

the first one changes the refund status to `Under Investigation` and the second one rejects it.

The loading depends primarily on the `bsplink-refund-management` service which in turn depends
(at this moment) on the services `bsplink-agent-management` and `bsplink-airline-management`
so in order to be able to load the file some configuration is needed in those services.

The file `postman/mass_load_test.postman_collection.json` contains a `Postman` collection
with the request necessary to prepare the services for the loading, every request in this
collection has a description explaining what it is for.

The steps to do the manual loading of the file are:

1. run the services:

    - `bsplink-refund-management`
    - `bsplink-agent-management`
    - `bsplink-airline-management`
  
2. execute the `Postman`'s requests.
3. execute the loader with the file.

**Note** that the example file `files/AAe9EARS_20170410_1234_001` was modified in order to be
able to do the loading but it was no modified to be coherent in the data that it contains.
