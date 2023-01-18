# User Management

### Short description
This project exposes a series of endpoints through a Spring Boot application to manipulate an User entity, composed by id, name, surname, address and mail, and stored into a PostgreSQL.

### Set up
To compile and run this project is necessary to execute:
- **mvn clean package** (creates the JAR file into Target package)
- **cd docker**
- **docker-compose up** (starts docker. Rebuild the image if necessary through --build)

### API Features

- CRUD endpoint on User entity
- **/getByName endpoint** to retrieve users by name, surname or both

  > Returns a list of users if at least one parameter
  > is not null or empty, otherwhise an empty list

- **/csv** endpoint to upload a CSV file with User info and store them into the database

  > Save CSV content in DB if the file is valid.
  > This endpoint provides both creation, in case "id" column in CSV is empty,
  > and update, in case "id" is valorized and present in the DB.
  > Otherwise, it ignores the record.
  > There's a CSV file to test this endpoint  