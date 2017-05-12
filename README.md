# Trigonometry API using Play Framework

This is an example of simple API written in Play/Scala consuming json and returning result in json.
It uses law of sines and cosines to calculate missing angles and sides. Cases accepted: ASA, SAA, SSA, SAS, SSS, where S is side and A is angle.

Technologies used:
 - Scala 2.11
 - Play 2.5
 - Swagger for auto-generated json documentation
 - scalatest and spec2 for testing


## Clone Project
```
git clone git@github.com:richardpi/rizn-play-scala-trigonometry-api.git
cd rizn-play-scala-trigonometry-api
sbt clean compile
```

## Run Project
To run the project use the below command which will start your play application at port 9000.
```
sbt run
```

##### How to use

* Endpoint is "/api/triangle"
* Angles are in degrees
* Ensure you pass header: Content-Type: application/json
* POST method is only accepted
* Pass payload as raw json

Example (SAA):

Request:

```
{
  "data": {
    "angleA": 25,
    "angleB": 20,
    "sideA": 80.4    
  }
}
```

Response:

```
{
    "result": {
        "sideC": 134.52,
        "angleB": 20,
        "angleC": 135,
        "sideB": 65.07,
        "sideA": 80.4,
        "angleA": 25
    }
}
```

Other cases:

ASA:

```
{
  "data": {
    "angleA": 75,
    "angleB": 60,
    "sideC": 340    
  }
}
```

SSA (case is prone to ambiguous case. It can return 1 or 2 triangles matching criteria or no triangle at all)

```
{
  "data": {
    "angleA": 43.1,
    "sideA": 186,
    "sideB": 250    
  }
}
```

SSS:

```
{
  "data": {
    "sideA": 5,
    "sideB": 8,
    "sideC": 12    
  }
}
```

SAS:

```
{
  "data": {
    "angleA": 46.5,
    "sideB": 10.5,
    "sideC": 18    
  }
}
```


##### Swagger documentation

Generated documentation is available (GET method):
```
http://localhost:9000/swagger.json
```

##### Code structure

- app/controllers/TrigController - controller responsible for accepting and returning data
- app/services - services calculating triangle data
- test - unit tests for trigonometry service and functional tests for controller

##### Live example:

To test API, go to url below (POST method and Content-Type: application/json). Please give couple of seconds to return response as we use free dyno on heroku:

```
http://trig.rizn.pl/api/triangle
```


##### Blog:

For more info, go to:

[http://www.prog.rizn.pl/posts/20](http://www.prog.rizn.pl/posts/20)
