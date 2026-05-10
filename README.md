# CarInspec — Vehicle Inspection Manager (Java / JavaFX)

Description
-----------------
CarInspec is a Java desktop application that manages vehicle records and inspection expiry dates and provides a simple admin-managed user list. It uses plain text files in `data/` for persistence and a JavaFX UI (`src/ui/MainApp.java`).

Install from GitHub
-------------------
1. Clone the repository:

```bash
git clone https://github.com/Rencikas/oop_tuv_project.git
```


Windows — install & run
-----------------------
1. Install JDK 17+ and add `java`/`javac` to PATH.


2. Download JavaFX SDK (matching your JDK) from https://gluonhq.com/products/javafx/ and extract (example path: `C:\java\javafx-sdk-26.0.1`).

3. Set `JAVAFX_SDK` for the session (Command Prompt):

```bat
set JAVAFX_SDK=C:\java\javafx-sdk-26.0.1
```

4. Use the run.bat script to build and run:

```bat
run.bat
```

If you prefer manual compilation (Command Prompt):

```bat
mkdir bin
javac --module-path "%JAVAFX_SDK%\lib" --add-modules javafx.controls,javafx.fxml -d bin (for %i in (src\**\*.java) do @echo %i)

java --module-path "%JAVAFX_SDK%\lib" --add-modules javafx.controls,javafx.fxml -cp bin ui.MainApp
```

macOS — install & run
---------------------
1. Install JDK 17+ (Adoptium, Azul, or Oracle). Verify:

```bash
java -version
javac -version
```

2. Download JavaFX SDK matching your JDK and extract (example: `~/Downloads/javafx-sdk-26.0.1`).

3. Set `PATH_TO_FX` for the session (zsh/bash):

```bash
export PATH_TO_FX="$HOME/Downloads/javafx-sdk-26.0.1/lib"
```

4. Use the run.sh script to build and run:

```bash
chmod +x ./run.sh
bash ./run.sh
```

Or manual compile & run:

```bash
mkdir -p bin
javac --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -d bin $(find src -name "*.java")

java --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -cp bin ui.MainApp
```

Notes
-----
- If you see errors about `package javafx.* does not exist`, make sure the `--module-path` points to the JavaFX SDK `lib` folder and `--add-modules javafx.controls,javafx.fxml` are present.
- The app reads/writes files in `data/`; ensure the running user has write permission.