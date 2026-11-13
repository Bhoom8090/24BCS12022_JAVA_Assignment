# Student Management System

A Spring Boot REST API with a web frontend for managing student records, using MySQL as the database.

---

## 1. Introduction to the Project

### About
The Student Management System lets a user **add, view, update, delete and search** student records. It has two parts:

- **Backend:** a Spring Boot REST API connected to a MySQL database using Spring Data JPA.
- **Frontend:** simple web pages (Thymeleaf + HTML/CSS) served by the embedded Tomcat servlet container, so the system can be used directly from the browser.

### Features
- Add, view, update and delete students (CRUD)
- Search students by course
- Input validation (required fields, valid email, age range)
- Duplicate email check
- Exception handling with meaningful JSON error messages
- Correct HTTP status codes (200, 201, 204, 400, 404, 409, 500)

### Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 17+ | Programming language |
| Spring Boot 3.3.5 | Application framework |
| Spring Web | REST API and MVC |
| Spring Data JPA (Hibernate) | Database operations |
| MySQL | Database |
| Thymeleaf, HTML, CSS | Frontend |
| Bean Validation | Input validation |
| Maven | Build tool |
| Postman | API testing |

---

## 2. Step by Step: How to Run the Project

### Requirements
- JDK 17 or above (check with `java -version`)
- MySQL Server installed and running on port 3306
- Maven, or VS Code with the "Extension Pack for Java" and "Spring Boot Extension Pack"

### Step 1: Get the project
Download or clone the project and open the folder that contains `pom.xml`.
```
git clone https://github.com/Bhoom8090/24BCS12022_JAVA_Assignment.git
cd 24BCS12022_JAVA_Assignment
```

### Step 2: Start MySQL
Make sure the MySQL server is running. You do **not** need to create the database or table manually; both are created automatically on first start.

### Step 3: Set the MySQL login
`src/main/resources/application.properties` reads the login from environment variables. If they are not set, the defaults `root` / `root` are used.

**Option A: set environment variables (recommended)**

Windows PowerShell:
```
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```
Linux / Mac:
```
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

**Option B: edit the defaults in `application.properties`**
```
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:your_mysql_password}
```

### Step 4: Run the application

**Using terminal (from the folder containing `pom.xml`):**
```
mvn spring-boot:run
```

**Using VS Code:** open `StudentManagementApplication.java` and click **Run** above the `main` method (or press F5).

Wait until the console shows:
```
Started StudentManagementApplication in ... seconds
```

### Step 5: Open the application
- Frontend: http://localhost:8080/
- REST API: http://localhost:8080/api/students

### Step 6: Test the API with Postman
1. Open Postman, click **Import** and select `postman/Student_API.postman_collection.json`.
2. Run **Add Student** first, then the other requests.

### Step 7: Verify the data in MySQL
```sql
USE student_db;
SELECT * FROM students;
```

### Step 8: Stop the application
Press `Ctrl + C` in the terminal, or click the red stop button in VS Code.

---

## 3. Database Schema

**Database name:** `student_db`
**Table name:** `students`

| Column | Data Type | Constraints | Description |
|--------|-----------|-------------|-------------|
| id | BIGINT | Primary Key, Auto Increment | Unique student ID |
| name | VARCHAR(255) | Not Null | Student name |
| email | VARCHAR(255) | Not Null, Unique | Student email address |
| course | VARCHAR(255) | Not Null | Enrolled course |
| age | INT | Not Null | Student age |

### SQL script
The database and table are created automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`). The same structure is available manually in `database/schema.sql`:

```sql
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE IF NOT EXISTS students (
    id      BIGINT       NOT NULL AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    course  VARCHAR(255) NOT NULL,
    age     INT          NOT NULL,
    PRIMARY KEY (id)
);
```

---

## 4. Other Relevant Information

### REST API Endpoints

| Method | Endpoint | Description | Success Status |
|--------|----------|-------------|----------------|
| POST | /api/students | Add a new student | 201 Created |
| GET | /api/students | Get all students | 200 OK |
| GET | /api/students/{id} | Get student by ID | 200 OK |
| PUT | /api/students/{id} | Update student details | 200 OK |
| DELETE | /api/students/{id} | Delete a student | 204 No Content |
| GET | /api/students/course/{course} | Get students by course | 200 OK |

### Sample request body (POST / PUT)
```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "course": "Computer Science",
  "age": 20
}
```

### Validation rules
- Name: required, 2 to 100 characters
- Email: required, valid format, must be unique
- Course: required
- Age: required, between 5 and 100

### Exception handling

| Status | Situation |
|--------|-----------|
| 400 Bad Request | Validation fails, invalid JSON or invalid ID format |
| 404 Not Found | Student with the given ID does not exist |
| 409 Conflict | Email already exists |
| 500 Internal Server Error | Any unexpected error |

Sample error response (404):
```json
{
  "timestamp": "2026-11-14T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Student not found with id: 999"
}
```

### Project structure
```
src/main/java/com/student/management
 |-- StudentManagementApplication.java
 |-- entity/         Student.java
 |-- repository/     StudentRepository.java
 |-- service/        StudentService.java
 |-- controller/     StudentRestController.java   (REST API)
 |                   StudentViewController.java   (frontend pages)
 |-- exception/      ResourceNotFoundException, DuplicateEmailException,
                     ErrorResponse, GlobalExceptionHandler
src/main/resources
 |-- application.properties    (database configuration)
 |-- templates/                students.html, student-form.html
 |-- static/css/style.css
database/schema.sql            (optional manual DB script)
postman/                       (Postman collection)
screenshots/                   (Postman and UI screenshots)
```

### Screenshots

| Endpoint | Screenshot |
|----------|-----------|
| POST /api/students | ![POST](screenshots/post.png) |
| GET /api/students | ![GET all](screenshots/get_all.png) |
| GET /api/students/{id} | ![GET by id](screenshots/get_by_id.png) |
| PUT /api/students/{id} | ![PUT](screenshots/put.png) |
| DELETE /api/students/{id} | ![DELETE](screenshots/delete.png) |
| GET /api/students/course/{course} | ![By course](screenshots/by_course.png) |
| Frontend | ![UI](screenshots/frontend.png) |
| MySQL table | ![DB](screenshots/mysql.png) |

### Common problems

| Problem | Solution |
|---------|----------|
| `Access denied for user 'root'` | Wrong MySQL password. Set `DB_PASSWORD` correctly (Step 3). |
| `Communications link failure` | MySQL server is not running. Start it (Step 2). |
| `Port 8080 already in use` | Stop the other running instance, or set `server.port=8081` in `application.properties`. |
| `mvn is not recognized` | Install Maven, or run the project from VS Code instead. |

### Future scope
- User login and authentication using Spring Security
- Pagination and sorting of student records
- Search by name or email
- Deployment on a cloud platform

### Conclusion
This project demonstrates a layered Spring Boot application with a REST API, MySQL connectivity using Spring Data JPA, validation, exception handling and a simple frontend.
