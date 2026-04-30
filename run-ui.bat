@echo off
REM Run the JavaFX UI (MainApp) on Windows
REM Requires: JDK 21+ with JavaFX SDK installed, and backend compiled

echo.
echo Starting CarInspec UI...
echo.

REM Check if JAVAFX_SDK is set
if not defined JAVAFX_SDK (
    echo.
    echo WARNING: JAVAFX_SDK environment variable is not set
    echo Please set it to your JavaFX SDK path before running this script
    echo.
    echo Example:
    echo   set JAVAFX_SDK=C:\javafx-sdk-21
    echo.
    echo After setting the variable, run this script again
    pause
    exit /b 1
)

REM Verify JavaFX SDK exists
if not exist "%JAVAFX_SDK%" (
    echo.
    echo Error: JavaFX SDK not found at: %JAVAFX_SDK%
    echo Please check that JAVAFX_SDK is set correctly
    pause
    exit /b 1
)

echo Using JavaFX SDK from: %JAVAFX_SDK%
echo.

REM Verify backend is compiled
if not exist "src\models\Vehicle.class" (
    echo Backend not compiled. Running build.bat first...
    echo.
    call build.bat
    if %ERRORLEVEL% neq 0 (
        echo.
        echo Backend compilation failed!
        pause
        exit /b 1
    )
    echo.
)

REM Compile UI module
echo Compiling MainApp.java...
javac -d src --module-path %JAVAFX_SDK%\lib --add-modules javafx.controls,javafx.fxml -cp src -encoding UTF-8 src\ui\MainApp.java

if %ERRORLEVEL% neq 0 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compilation successful!
echo.

REM Run the application from project root (so data/ path is correct)
echo Running MainApp...
java --module-path %JAVAFX_SDK%\lib --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics -cp src ui.MainApp

if %ERRORLEVEL% neq 0 (
    echo.
    echo Application exited with error code: %ERRORLEVEL%
    pause
    exit /b 1
)
