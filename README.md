# 🔍 Lost & Found Management System

🚀 **A Java Swing-based desktop application that solves real-world campus item tracking using a structured claim system and role-based access control.**

---

## ▶️ Run Instantly (No Setup Required)

```bash
java -jar LostAndFound.jar
```

📥 Download the `.jar` file from this repository and run it directly.

---

## 🎯 Problem Statement

In campuses and organizations, items like ID cards, bags, keys, and devices are frequently lost, but there is **no centralized system** to track or recover them.

👉 This project provides a **digital solution** where users can report, search, and claim lost/found items efficiently.

---

## 💡 Key Features

- 🔐 Role-based system (Student & Admin)  
- 📦 Report Lost / Found items  
- 🔎 Browse & filter items  
- 📩 Claim request system with tracking  
- 🛠 Admin dashboard for full control  
- 💾 Persistent storage using JSON (no database required)  
- 🎨 Interactive GUI built using Java Swing  

---

## 🧠 Tech Stack

- **Java**
- **Java Swing (GUI)**
- **JSON (File-based storage)**
- **Object-Oriented Programming (OOP)**

---

## 🏗️ Architecture

```
GUI Layer        → Login, Dashboards, Dialogs  
Manager Layer    → Business Logic (AppManager)  
Model Layer      → User, Item, ClaimRequest  
Utility Layer    → JSON Parser, UI Theme, ID Generator  
```

---

## 🧩 OOP Concepts Used

| Concept | Implementation |
|--------|--------------|
| Encapsulation | Private fields with getters/setters |
| Inheritance | Student & Admin extend User |
| Polymorphism | Method overriding (getRole, hasPermission) |
| Abstraction | Actionable interface |
| Association | ClaimRequest links User ↔ Item |

---


## 🔑 Default Login

| Role | Email | Password |
|------|------|----------|
| Admin | admin@university.edu | admin123 |

> ⚠️ Default admin is auto-created on first run.

---

## 📁 Project Structure

```
src/
 ├── gui/
 ├── manager/
 ├── model/
 ├── interfaces/
 ├── util/
 └── Main.java

screenshots/        (Add images here)
LostAndFound.jar    (Runnable file)
README.md
```

---

## 🚀 How It Works

- Users report lost or found items  
- Items are stored in JSON files  
- Users can submit claim requests  
- Admin verifies and approves/rejects claims  
- Data persists across sessions  

---
## 🎥 Demo

Watch the full working demo here:
👉 https://drive.google.com/file/d/1HNDPAX5QU_kg-r6zh_QI7PEWjYh6XNPi/view?usp=sharing

---

## 🎯 Why This Project Matters

This project demonstrates how **core OOP principles + clean architecture** can be used to build a **real-world, usable application** without relying on external databases.

---

## 👨‍💻 Author

**Pranav Dixit**  
B.Tech CSE (AI/ML)

---

## ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub!
