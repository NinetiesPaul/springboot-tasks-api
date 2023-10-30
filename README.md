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
```
curl --location 'http://localhost:8080/api/users/register' \
--header 'Content-Type: application/json' \
--data '{
    "name": "T. Soprano",
    "username": "t.soprano",
    "password": "password"
}'
```

#### User authentication
```
curl --location 'http://localhost:8080/api/users/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "t.soprano",
    "password": "password"
}'
```

### __Tasks__

#### Creating a new Task
```
curl --location 'http://localhost:8080/api/task/create' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {token}' \
--data '{
    "title": "New Task again",
    "description": "Here is a description",
    "type": "feature"
}'
```

#### Listing all Tasks
Replace ```{created_by}``` with a User Id  
Replace ```{status}``` with any of the following: new, in_dev, in_qa, blocked, closed  
Replace ```{type}``` with any of the following: feature, bugfix, hotfix  
```
curl --location 'http://localhost:8080/api/task/list?type={type}&status={status}&created_by={created_by}' \
--header 'Authorization: Bearer {token}'
```

#### Selecting a single Task
```
curl --location 'http://localhost:8080/api/task/view/{taskId}' \
--header 'Authorization: Bearer {token}'
```

#### Updating a Task
```
curl --location --request PUT 'http://localhost:8080/api/task/update/{taskId}' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {token}' \
--data '{
    "type": "hotfix"
}'
```

#### Deleting a Task
```
curl --location --request DELETE 'http://localhost:8080/api/task/delete/{taskId}' \
--header 'Authorization: Bearer {token}'
```

#### Closing a Task
```
curl --location --request PUT 'http://localhost:8080/api/task/close/{taskId}' \
--header 'Authorization: Bearer {token}'
```