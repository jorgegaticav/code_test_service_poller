# Kry/Livi Code Assignment

## Summary

As a part of scaling the number of services running within a modern health
tech company we need a way to make sure our systems are running
smoothly. None of the monitoring tools that we have looked at satisfy our
requirements so we have decided that we will build one ourselves. What we
want you to do is to build a simple service poller that keeps a list of
services (defined by a URL), and periodically performs a HTTP GET request
to each and stores a record of the response ("OK" or "FAIL"). Apart from the
polling logic we want to have all the services visualised and easily managed
in a basic UI presenting all the services together with their status.

## Requirements
#### Basic requirements (If these aren’t met the assignment will not pass):
- [X] A user needs to be able to add a new service with URL and a name
- [X]  Added services have to be kept when the server is restarted
- [X]  Display the name, url, creation time and status for each service
- [X]  Provide a README in english with instructions on how to run the
application

#### Extra requirements (No prioritisation on these, pick the ones that you find interesting):
- [X]  We want full create/update/delete functionality for services
- [X]  The results from the poller are automatically shown to the user (no
need to reload the page to see results)
- [ ]  We want to have informative and nice looking animations on
add/remove services
- [ ]  The service properly handles concurrent writes
- [ ]  Protect the poller from misbehaving services (for example answering
really slowly)
- [X]  URL Validation ("sdgf" is probably not a valid service)
- [ ]  Multi user support. Users should not see the services added by
another user

# Instructions

## Initialize MySQL Database

To start the MySQL database in a docker container, run:
```
docker-compose -f src/main/docker/mysql.yml up -d
```
Wait for the container to be ready

## Build & Run Java Server

Run the gradle wrapper to build and start the Java server on `locahost:8080`.

```
./gradlew clean run
```

## Run Client

Go to client directory:

```
cd ./src/main/webapp/
```

Install dependencies (`node_modules` folder):

```
npm install
```
Start React application on `localhost:3000` using:
```
npm start
```