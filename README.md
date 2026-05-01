# CarInspec

**CarInspec** is a Java desktop application for managing vehicles, user accounts, and inspection records.

---

## ✨ Features

* **User Authentication** — Register and log in securely
* **Vehicle Management** — Add, edit, and manage vehicles
* **Search & Filter** — Find vehicles by license plate, make, color, fuel type, or category
* **Inspection Tracking** — Track and manage vehicle inspection dates

---

## 📦 Requirements

* **Java 21+** (JDK, not JRE)
* **JavaFX SDK 21+**

---

## ⚙️ Installation

### 1. Install Java (JDK 21)

* Download from: [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
* Install using default settings

---

### 2. Download JavaFX SDK

* Download from: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
* Extract to a directory, for example:

```
C:\java\javafx-sdk-21
```

---

### 3. Set Environment Variable

Open **Command Prompt** and run:

```bat
set JAVAFX_SDK=C:\java\javafx-sdk-21
```

> 💡 Tip: For permanent usage, add `JAVAFX_SDK` as a system environment variable.

---

## ▶️ Running the Application

Run the following command in the project's directory:

On Linux:
```bash
./run.bat
```
On Windows:
```bash
run.bat
```

The application will automatically build and launch.

---

## 📁 Project Structure

```
oop_tuv_project-main/
├── src/                 # Backend and UI source code
├── data/                # Application data storage
├── run.bat              # Build and run script
└── README.md            # Project documentation
```

---

## 🔐 Demo Credentials

You can use these accounts for testing:

### User

* Username: `User`
* Password: `test`

### Admin

* Username: `Admin`
* Password: `test`

---

## 📝 Notes

* Ensure Java and JavaFX are installed correctly before running the application
* Verify that the `JAVAFX_SDK` path is correct if the app fails to start
* Designed to be simple, clean, and easy to use

---
