<h1 align="center">
  <br>
  UniFind - Central Campus Lost and Found 🏫
</h1>

<div align="center">
  <strong>A modern, AI-powered platform to streamline reporting, finding, and claiming lost items on university campuses.</strong>
</div>

<br>

<div align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white" alt="GCP" />
  <img src="https://img.shields.io/badge/Gemini_AI-8E75B2?style=for-the-badge&logo=google-gemini&logoColor=white" alt="Gemini" />
  <img src="https://img.shields.io/badge/Render-%2346E3B7.svg?style=for-the-badge&logo=render&logoColor=white" alt="Render" />
</div>

<br>

## 🌟 Live Demo
The application is currently live and deployed in the cloud!  
👉 **[Visit UniFind Live](https://unifind1.onrender.com)**

---

## ✨ Key Features
- 🤖 **AI-Powered Item Matching:** Integrates with Google Gemini AI to analyze item descriptions and automatically suggest potential matches between lost and found items.
- 🔐 **Secure Authentication:** Integrated with **Google OAuth2** so students can log in seamlessly using their existing university Google accounts.
- 📸 **Cloud Image Storage:** Uploaded photos of lost items are securely stored and served globally via **Cloudinary**.
- ☁️ **Cloud Database:** Real-time data persistence using a fully managed **Aiven MySQL** database.
- 🎨 **Responsive UI:** A beautiful, accessible, and mobile-friendly frontend crafted with HTML5, CSS3, Thymeleaf, and FontAwesome.
- 📊 **Admin Dashboard:** Specialized management views for campus administrators to oversee reports and claim requests.

---

## 🛠️ Tech Stack & Architecture

### Backend & Core
- **Java 17** & **Spring Boot 3**
- **Spring Security** (OAuth2 Authentication)
- **Spring Data JPA / Hibernate** (ORM)
- **Maven** (Dependency Management)

### Frontend
- **Thymeleaf** (Server-side templating)
- **HTML5 & Vanilla CSS3** (Custom vibrant design system)
- **FontAwesome** (Icons)

### Cloud Infrastructure & Third-Party APIs
- **Hosting:** Render.com (Dockerized Container Deployment)
- **Database:** Aiven (Managed MySQL)
- **Image Storage:** Cloudinary SDK
- **AI Integration:** Google Gemini Pro API

---

## 🚀 Running Locally (For Developers)

Want to run the code on your own laptop? Follow these steps:

### 1. Prerequisites
- **Java 17** or higher installed
- **Git** installed

### 2. Clone the Repository
```bash
git clone https://github.com/janveryuu/unifind.git
cd unifind
```

### 3. Setup Environment Variables
To run all the features (like Google Login, Gemini AI, and Cloudinary), you need to provide your own API keys. Create an `application-local.properties` or set these in your terminal environment:
- `GEMINI_API_KEY`
- `GOOGLE_CLIENT_ID`
- `GOOGLE_CLIENT_SECRET`
- `CLOUDINARY_URL`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### 4. Run the Application
You can use the included Maven wrapper to start the server:
```bash
# On Mac/Linux
./mvnw spring-boot:run

# On Windows
mvnw.cmd spring-boot:run
```

### 5. Open Your Browser
Go to [http://localhost:8081](http://localhost:8081) and you will see the UniFind landing page!

---

## 👨‍💻 About the Developer
This project was developed as part of a collaborative academic group project and is now being showcased as part of my professional development portfolio.

*Developed and engineered by Jhan-Jhan17*
