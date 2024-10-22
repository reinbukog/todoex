# Tasks API

#### Quick Summary

This is a SpringBoot project built for basic CRUD for Tasks

#### Version

`0.0.1-SNAPSHOT`

### How do I get set up? ###

#### Pre-requisites

* Install Java 17; `java -version` should be version 17+
* Install Maven; `mvn --version` should show Java version 17+
* Install Docker and docker-compose

### Building and Running

There are 2 ways in running the mini app, non-docker and with Docker. 

#### Non-Docker 

* #### Building

```shell
mvn clean install -Dmaven.test.skip=true 
or 
mvn clean package 
or 
./mvnw clean package
```

* #### Running

```shell
mvn spring-boot:run
```

* #### Testing

```shell
mvn test
```

#### Using Docker

* #### Building

```shell
docker-compose build
```

* #### Running

```shell
docker-compose up
```

The `./run.sh` file facilitates the build and run procedure of the application.

```shell
./run.sh
```

### Tasks API ###
CRUD operations for Tasks. Following are the sample requests:\

Swagger: `http://localhost:8080/swagger-ui/index.html`
* Create a new task. `POST /tasks`
  * Sample endpoint: `http://localhost:8080/tasks`
  * Sample request and response:
```json
{
    // Request
    "title": "Task 1",
    "description": "Clean the living Room",
    "isCompleted": false
}
```
```json 
{
    // Response
    "id": "4d4d7c31-455c-44a2-bdbe-197af21f59fb",
    "title": "Task 1",
    "description": "Clean the living Room",
    "isCompleted": false
}
```
* Retrieve list of tasks. `GET /tasks`
  * Sample endpoint: `http://localhost:8080/tasks`
  * Sample Response:
```json
{
    "content": [
        {
            "id": "4d4d7c31-455c-44a2-bdbe-197af21f59fb",
            "title": "Task 1",
            "description": "Clean the living Room",
            "isCompleted": false
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 1,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
}
```
* Retrieve specific task by ID. `GET /tasks/{id}`
  * Sample endpoint: `http://localhost:8080/tasks/4d4d7c31-455c-44a2-bdbe-197af21f59fb`
  * Sample Response:
```json
{
    // Response
    "id": "4d4d7c31-455c-44a2-bdbe-197af21f59fb",
    "title": "Task 1",
    "description": "Clean the living Room",
    "isCompleted": false
}
```
* Update specific task by ID. `PUT /tasks/{id}`
  * Sample endpoint: `http://localhost:8080/tasks/4d4d7c31-455c-44a2-bdbe-197af21f59fb`
  * Sample Request and Response
```json
// Request and Response
    "id": "4d4d7c31-455c-44a2-bdbe-197af21f59fb",
    "title": "Task 1",
    "description": "Clean the living Room - COMPLETED",
    "isCompleted": true
```
* Delete specific task by ID. `DELETE /tasks/{id}`
  * Sample endpoint: `http://localhost:8080/tasks/4d4d7c31-455c-44a2-bdbe-197af21f59fb`