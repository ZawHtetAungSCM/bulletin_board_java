# Bulletin Board (Java - Servlet)

## Overview

This servlet project implements a simple bulletin board system where users can create, read, update, and delete posts. Additionally, users can be managed through CRUD operations.

## Getting Started

### 1. Clone the Repository

```

git clone https://github.com/ZawHtetAungSCM/bulletin_board_java.git

```

### 2. Build and Run

1. Import the project into your preferred IDE.

2. Update the database connection details in `./constants/DbConstants.java` and run PostgreSQL Server.
> Database migration will be done automatically

> Make sure same database name is not exist in SQL server. If not, migration will not work.

3. Configure the servlet container (e.g., Apache Tomcat).

4. Run the project on the servlet container.

### 3. Access the Application

Open a web browser and navigate to http://localhost:8080/bulletin_board.

Login using this Account
```
email : admin@gmail.com
password : password
```

## Dependencies

Java: JDK 8 or later
Servlet Container: Apache Tomcat (or any other compatible container)
Database: PostgreSQL
