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
