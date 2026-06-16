# FoundIt - Central Campus Lost and Found

Welcome to **FoundIt**, a centralized Lost and Found system designed specifically for campus environments. This project was developed to streamline the process of reporting, finding, and claiming lost items within a university campus, leveraging a modern web tech stack.

## Features
- **Item Database**: Easily browse reported lost items with a user-friendly interface.
- **Reporting System**: Submit new found items with images, locations, and descriptions.
- **User Authentication**: Secure Google OAuth2 login integration.
- **Responsive UI**: A clean, accessible frontend crafted with HTML, CSS, and Thymeleaf.
- **Database Management**: Stores records using MySQL (or H2 for testing/development).
- **Admin Dashboard**: Specialized views for administrators to manage reports.

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Security
- **Frontend**: HTML5, CSS3, Thymeleaf, FontAwesome
- **Database**: MySQL, Spring Data JPA / Hibernate
- **Build Tool**: Maven

## How to Run the Project Locally

### 1. Database Setup
This application is configured out-of-the-box to use an **in-memory H2 database** for rapid testing and easy portfolio demonstration. This means you do not need to install MySQL or XAMPP to run it locally.
*If you wish to connect it to a persistent MySQL database, simply uncomment the MySQL settings in `application.properties`.*

### 2. Running the Application
Ensure you have Java 17+ installed.
1. Open a terminal/command prompt in the project's root folder.
2. Run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(On Windows Command Prompt, use `mvnw.cmd spring-boot:run`)*

### 3. Accessing the Website
Once the Spring Boot application has started, open your web browser and go to:
[http://localhost:8080](http://localhost:8080)

You should now see the FoundIt landing page and be able to interact with the software.

## About the Author
This project was developed as part of a collaborative academic group project and is now being showcased as part of my professional development portfolio.

---
*Developed by Jhan-Jhan17*
