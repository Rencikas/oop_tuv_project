# CarInspec - Vehicle Management System

A Java desktop application for managing vehicles and user authentication.

## Features

- **User Login/Registration** - Create accounts and log in securely
- **Vehicle Management** - Add, update, and manage vehicles  
- **Search & Filter** - Find vehicles by license plate, make, color, fuel type, or category
- **Inspection Tracking** - Track vehicle inspection dates

## Requirements

- **Java 21+** (JDK, not JRE)
- **JavaFX SDK 21+**

## Installation

1. **Install Java:**
   - Download JDK 21 from [https://www.oracle.com/java/technologies/downloads/](https://www.oracle.com/java/technologies/downloads/)
   - Install it (use default path)

2. **Download JavaFX SDK:**
   - Download from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
   - Extract to a folder (e.g., `C:\javafx-sdk-21`)

3. **Set Environment Variable:**
   - Open Command Prompt
   - Run: `set JAVAFX_SDK=C:\path\to\your\javafx-sdk-21`

## Running the Application

```bash
quick-start.bat
```

That's it! The app will build and launch.

## Project Structure

```
oop_tuv_project-main/
├── src/                 Backend source code
├── ui/                  JavaFX user interface
├── data/                Application data storage
├── bin/                 Compiled files (created during build)
├── build.bat            Compile backend
├── quick-start.bat      Build and run
└── README.md            This file
```

## Default Test User

- **Username:** `admin`
- **Password:** `admin123`

---

**Simple, clean, and ready to go!**
