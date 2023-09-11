## Tasks API in Spring Boot
A simple API in Spring Boot. It has some basic functions, like:

- User registration and authentication using JWT
- Perform CRUD operations of Tasks
- Some basics business rules enforced

My main goal is to showcase some concepts, such as:

- Migrations and model relationship
- Authentication configuration
- Routes definitions
- SOLID and KISS principles on a MVC architecture

The main tech behind it is Spring Boot and also some others Java libraries. For storaging, i'm using MySQL relational database

## Environment setup
To run this application you must have installed on your environment:

* `Java` - For the main application 
* `Maven` - For library and packages management
* `MySQL` - For storaging and accessing data

## Installation and Configuration
Once you set up all necessary software and tools, run
```
cp src/main/resources/application.properties.local src/main/resources/application.properties
```
and fill out the bracketed values on that new copy's spring.datasource enviroment variables. Don't forget to set up a crypt hash for the ```jwt.secret```.
Then run
```
mvn spring-boot:run
```
to install all packages and run the application. If no errors is shown and the CLI shows the server is running, you're good to go!

## Usage

### __Users__

#### User creation

#### User authentication

### __Tasks__

#### Creating a new Task

#### Listing all Tasks

#### Updating a Task

#### Deleting a Task

#### Closing a Task
