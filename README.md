# jetty-restful-app

## How to run:

```sh
./gradlew jar
cd build/libs
java -jar demo-restful-app-1.0-SNAPSHOT.jar --port 8081 --threads 10
``` 
please take notice of the fact that to compile source code and run the application your should have **jdk 8** installed on your computer 

## REST API

### Create a new account

request:
```
POST /accounts
Content-Type: application/json
{
  "id": 1,
  "money": 10.0
}

```
response:

```
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
  "status": "success",
  "data": {
    "id": 1,
    "money": 20.0
  }
}
```

### Create a transaction

request:
```
PUT /accounts
Content-Type: application/json

{
    "from": 1,
    "to": 2,
    "money": 20.0
}
```
response:
```
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
    "status": "ok",
    "data":{
        "from": 1,
        "to": 2,
        "money": 20.0
    }
}
```


### Get an account information

request:

```
GET /accounts/1
```

response:
```
HTTP/1.1 200 OK
Content-Type: application/json;charset=utf-8

{
  "status": "ok",
  "data": {
    "id": 1,
    "money": 20.0
  }
}
```
