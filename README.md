🔍 Lost & Found Management System

🚀 A Java Swing-based desktop application that solves real-world campus item tracking using a structured claim system and role-based access control.

▶️ Run Instantly (No Setup Required)
java -jar LostAndFound.jar

📥 Download the .jar file from this repository and run it directly.

🎯 Problem Statement

Lost items like ID cards, bags, and devices are common in campuses, but there is no centralized system to manage them.

👉 This project provides a digital solution to report, track, and recover lost items efficiently.

💡 Key Features
🔐 Role-based system (Student & Admin)
📦 Report Lost / Found items
🔎 Browse & filter items
📩 Claim request system with status tracking
🛠 Admin dashboard for full control
💾 Persistent storage using JSON (no database needed)
🎨 Interactive GUI built with Java Swing
🧠 Tech Stack
Java
Java Swing (GUI)
JSON (File-based storage)
Object-Oriented Programming (OOP)
🏗️ Architecture
GUI Layer        → Login, Dashboards, Dialogs  
Manager Layer    → Business Logic (AppManager)  
Model Layer      → User, Item, ClaimRequest  
Utility Layer    → JSON Parser, UI Theme, ID Generator  
🧩 OOP Concepts Used
Concept	Implementation
Encapsulation	Private fields + getters/setters
Inheritance	Student & Admin extend User
Polymorphism	Method overriding (getRole, hasPermission)
Abstraction	Actionable interface
Association	ClaimRequest links User ↔ Item

🔑 Default Login
Role	Email	                Password
Admin	admin@university.edu    admin123
	
📁 Project Structure
src/
 ├── gui/
 ├── manager/
 ├── model/
 ├── interfaces/
 ├── util/
 └── Main.java


🚀 Why This Project Matters

This project demonstrates how core OOP principles can be applied to build a real-world, usable application with clean architecture and persistent data handling.

👨‍💻 Author

Pranav Dixit
B.Tech CSE (AI/ML)

⭐ Show Your Support

If you found this project useful, consider giving it a ⭐ on GitHub!
