# 🔍 Lost & Found Management System
### Java Swing Desktop Application — Full OOP Implementation

---

## 📋 Overview
A complete Lost & Found Management System built in **pure Java** using **Swing GUI** with persistent file-based storage. Designed for university campus use.

---

## 🚀 How to Run

### Prerequisites
- **Java JDK 8 or higher** must be installed
  - Windows: Download from https://adoptium.net/
  - Ubuntu/Debian: `sudo apt install default-jdk`
  - macOS: `brew install openjdk`


### Option 1: Manual Compile + Run
```bash
# Step 1: Create output directory
mkdir -p out

# Step 2: Compile (from LostAndFound/ directory)
javac -d out \
  src/interfaces/Actionable.java \
  src/model/User.java src/model/Student.java \
  src/model/Admin.java src/model/Item.java \
  src/model/ClaimRequest.java \
  src/util/*.java src/manager/*.java \
  src/gui/*.java src/Main.java

# Step 3: Run
cd out && java Main
```

### Option 2: Run JAR (after building)
```bash
java -jar LostAndFound.jar
```

---

## 🔑 Default Login Credentials

| Role    | Email                    | Password   |
|---------|--------------------------|------------|
| Admin   | admin@university.edu     | admin123   |
| Student | Register via the app     | Your choice|

> ⚠️ The default admin is auto-created on first launch if no admin exists.

---

## 📁 Project Structure

```
LostAndFound/
├── src/
│   ├── Main.java                    ← Application entry point
│   ├── interfaces/
│   │   └── Actionable.java          ← ABSTRACTION: Interface
│   ├── model/
│   │   ├── User.java                ← ABSTRACT CLASS (parent)
│   │   ├── Student.java             ← INHERITANCE: extends User
│   │   ├── Admin.java               ← INHERITANCE: extends User
│   │   ├── Item.java                ← ENCAPSULATION: Item model
│   │   └── ClaimRequest.java        ← ASSOCIATION: User ↔ Item
│   ├── manager/
│   │   ├── AppManager.java          ← Business logic layer
│   │   └── DataManager.java         ← File I/O (Backend)
│   ├── gui/
│   │   ├── LoginFrame.java          ← Login window
│   │   ├── RegisterDialog.java      ← Student registration
│   │   ├── StudentDashboard.java    ← Student main window
│   │   ├── AdminDashboard.java      ← Admin main window
│   │   └── ClaimDialog.java         ← Claim submission dialog
│   └── util/
│       ├── UITheme.java             ← Centralized UI styling
│       ├── IDGenerator.java         ← Unique ID generation
│       ├── JsonParser.java          ← Manual JSON parser
│       └── RoundedBorder.java       ← Custom UI component
├── data/                            ← Auto-created on first run
│   ├── users.json                   ← Persistent user storage
│   ├── items.json                   ← Persistent item storage
│   └── claims.json                  ← Persistent claim storage
├── build_and_run.bat                ← Windows build script
├── build_and_run.sh                 ← Linux/macOS build script
└── README.md
```

---

## 🧠 OOP Concepts Demonstrated

### 1. ENCAPSULATION
All model fields are `private`. Access only through `getters/setters`.
```java
// In User.java, Item.java, ClaimRequest.java:
private String userId;        // Cannot access directly
public String getUserId() { return userId; }    // Getter
public void setName(String n) { this.name = n; } // Setter
```

### 2. INHERITANCE
`Student` and `Admin` both **extend** `User`:
```java
public class Student extends User { ... }
public class Admin extends User { ... }
// Both inherit: userId, name, email, password, phone
// Both add their own fields and override methods
```

### 3. POLYMORPHISM
Same method name, different behavior per class:
```java
student.getRole()         → "STUDENT"
admin.getRole()           → "ADMIN"

student.hasPermission("REMOVE_ITEM")  → false
admin.hasPermission("REMOVE_ITEM")    → true
```

### 4. ABSTRACTION
`Actionable` interface defines **what** without **how**:
```java
public interface Actionable {
    void performAction(String action);
    String getRole();
    boolean hasPermission(String permission);
}
// User implements this; Student and Admin provide the implementations
```

### 5. ASSOCIATION
Objects relate to each other through IDs (loose coupling):
```java
ClaimRequest {
    String itemId;           // → references Item
    String claimantUserId;   // → references User
}
// ClaimRequest ASSOCIATES User and Item
```

---

## 🖥️ GUI Features

### Login Window
- Toggle between Student / Admin login modes
- Input validation with error messages
- Direct link to student registration

### Student Dashboard (3 tabs)
| Tab | Features |
|-----|---------|
| 📦 View Items | Browse all items, filter by LOST/FOUND/CLAIMED, claim items, double-click for details |
| ➕ Report Item | Form to add lost or found items with category, location, description |
| 📋 My Claims | Track all submitted claim requests with color-coded status |

### Admin Dashboard (3 tabs)
| Tab | Features |
|-----|---------|
| 📊 Dashboard | Live statistics: total items, lost, found, claimed, pending claims, users |
| 📦 Manage Items | View all items, remove items, view full details |
| 📋 Manage Claims | View all claims, approve with note, reject with reason, color-coded status |

---

## 🗄️ Data Persistence (Backend)

Data is stored in JSON files in the `data/` folder:

**users.json** example:
```json
[
  {"userId":"USR123","name":"John Doe","email":"john@uni.edu",
   "password":"pass123","phone":"9876543210","role":"STUDENT",
   "studentId":"CS2021001","department":"Computer Science","yearOfStudy":3}
]
```

**items.json** example:
```json
[
  {"itemId":"ITM456","title":"Blue Backpack","description":"Nike bag with laptop",
   "category":"Accessories","location":"Library","dateReported":"2024-01-15",
   "type":"LOST","status":"ACTIVE","reportedByUserId":"USR123",
   "reportedByName":"John Doe","contactInfo":"9876543210"}
]
```

**claims.json** example:
```json
[
  {"claimId":"CLM789","itemId":"ITM456","itemTitle":"Blue Backpack",
   "claimantUserId":"USR456","claimantName":"Jane Smith",
   "status":"PENDING","adminNote":""}
]
```

> Data is saved automatically after every operation and loaded on startup.

---

## ✅ Feature Checklist

- [x] Student and Admin user roles
- [x] Login with role-based access control
- [x] Student registration with validation
- [x] Add Lost / Found items
- [x] View and filter items (LOST / FOUND / CLAIMED)
- [x] Claim an item with reason description
- [x] View my claims with status tracking
- [x] Admin: View all items and claims
- [x] Admin: Approve claims (marks item as CLAIMED, rejects other claims)
- [x] Admin: Reject claims with reason
- [x] Admin: Remove items
- [x] Admin: Dashboard statistics
- [x] Persistent storage (JSON files)
- [x] Input validation throughout
- [x] Confirmation dialogs for destructive actions
- [x] Double-click to view full item/claim details
- [x] Color-coded claim statuses
- [x] All OOP concepts clearly implemented and commented

---

## 👨‍💻 OOP Quick Reference for Viva

| Question | Answer |
|----------|--------|
| What is a class? | A blueprint. `Item`, `User`, `ClaimRequest` are classes |
| What is encapsulation? | Hiding data using `private` fields + public getters/setters |
| What is inheritance? | `Student extends User` — Student IS-A User |
| What is polymorphism? | `getRole()` returns "STUDENT" or "ADMIN" based on object type |
| What is abstraction? | `Actionable` interface hides implementation details |
| What is association? | `ClaimRequest` holds references to both `User` and `Item` |
| Where is data stored? | JSON files in `data/` folder — loaded on start, saved on change |
| What is an interface? | A contract — classes that implement it MUST provide those methods |
| Why use abstract class? | `User` can't be instantiated directly — must use `Student` or `Admin` |
| What is an ArrayList? | Dynamic array — used internally in `AppManager` for all data |
