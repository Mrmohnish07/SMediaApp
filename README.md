# SMediaApp
# 📱 Social Media Web Application

A full-stack **Social Media App** built using Java, Spring Boot, Thymeleaf, MySQL, and WebSocket. This project replicates core functionalities similar to popular social platforms, including user registration, login, post sharing, following users, real-time messaging, and notification system.

---

## 🌐 Live Demo

🚧 (Optional: Add live demo URL if hosted on Render, Heroku, or your domain)

---

## 🎯 Features

- 🔐 User Registration and Login (with password encryption)
- 👤 User Profile (with follower & following count)
- 📝 Create, Edit, and Delete Posts
- 🧵 Real-Time Chat (via WebSocket)
- 🔔 Notifications on Follow
- 💬 Comments and Likes (planned)
- 🧑‍🤝‍🧑 Suggested Users to Follow
- 📷 Profile Pictures (optional)
- 📱 Responsive Frontend (Thymeleaf + Bootstrap)

---

## 💻 Tech Stack

| Layer              | Technology Used                          |
|--------------------|-------------------------------------------|
| **Frontend**        | Thymeleaf, HTML, CSS, Bootstrap, JS       |
| **Backend**         | Java 11/17, Spring Boot                   |
| **WebSocket**       | Spring WebSocket, STOMP, SockJS           |
| **Database**        | MySQL                                     |
| **Messaging Format**| JSON                                      |
| **ORM**             | Hibernate / JPA                           |
| **Security**        | Spring Security (or custom session logic) |
| **Build Tool**      | Maven                                     |
| **IDE**             | IntelliJ IDEA / Eclipse                   |
| **Version Control** | Git & GitHub                              |

---

## 📂 Project Structure


---

## 🧠 Key Concepts Implemented

- MVC Architecture
- Spring Boot + Thymeleaf Integration
- Entity Relationships (OneToMany, ManyToMany)
- JPA Queries & Custom Queries
- WebSocket Integration for Real-Time Chat
- User Follow/Unfollow Logic with Validation
- Exception Handling with `@ControllerAdvice`
- Frontend Pagination and User Suggestions
- Modular Codebase following SOLID Principles

---

## 🗃️ Database Tables

- `users` – Stores user credentials and profile
- `posts` – Stores posts created by users
- `followers` – Stores follow relationships
- `messages` – Stores chat messages
- `notifications` – Stores system-generated alerts

---

## 🔧 How to Run Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/social-media-app.git
