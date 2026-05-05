# CarInspec

CarInspec is a Java-based desktop application designed for managing vehicle inventories, user authentication, and technical inspection records.

## Core Functionality

**Authentication System**
Provides secure user registration and login with role-based access.

**Inventory Management**
Supports full CRUD (Create, Read, Update, Delete) operations for vehicle records.

**Advanced Querying**
Enables search and filtering by license plate, manufacturer, color, fuel type, and vehicle category.

**Compliance Tracking**
Tracks and manages mandatory vehicle inspection schedules.

## Prerequisites

Ensure the following dependencies are installed before running the application:

* **Java Development Kit (JDK):** Version 21 or higher
* **JavaFX SDK:** Version 21 or higher

## Installation and Configuration

### 1. Install Java Development Kit

Download and install JDK 21 from the official Oracle website. Ensure it is added to your system PATH.

### 2. Configure JavaFX SDK

Download the JavaFX SDK from Gluon and extract it to a local directory, for example:

```
C:\java\javafx-sdk-21
```

### 3. Set Environment Variables

Define the `JAVAFX_SDK` environment variable.

**Windows (temporary session):**

```
set JAVAFX_SDK=C:\java\javafx-sdk-21
```

For a persistent setup, add `JAVAFX_SDK` through the System Environment Variables in the Control Panel.

## Execution

Navigate to the project root directory and run the initialization script.

**Linux/macOS:**

```
chmod +x run.bat
./run.bat
```

**Windows:**

```
.\run.bat
```

## Directory Structure

```
oop_tuv_project-main/
├── .vscode/             # IDE configuration
├── bin/                 # Compiled output
├── data/                # Application data
├── src/                 # Source code (backend and UI)
├── .gitignore           # Git ignore rules
├── run.bat              # Execution script
└── README.md            # Documentation
```

## Authentication Credentials

Pre-configured accounts for testing:

**Standard User**

* Username: `User`
* Password: `test`

**Administrator**

* Username: `Admin`
* Password: `test`

## Technical Notes

* **Environment Configuration:** Ensure `JAVA_HOME` and `JAVAFX_SDK` are correctly set.
* **Runtime Issues:** If the application fails to start, verify that JavaFX modules are properly referenced in `run.bat`.
* **Architecture:** The application follows a modular structure with a focus on maintainable backend logic and a clean user interface.
