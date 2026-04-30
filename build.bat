@echo off
REM Build script for Vehicle Management System (Windows)

echo Creating build directory...
if not exist bin mkdir bin

echo Compiling Java files...
javac -d bin -encoding UTF-8 ^
    src\Application.java ^
    src\models\*.java ^
    src\services\*.java ^
    src\persistence\*.java ^
    src\util\*.java

if %ERRORLEVEL% equ 0 (
    echo.
    echo Compilation successful!
    echo.
    echo Services available:
    echo   - AuthenticationService
    echo   - VehicleService
    echo   - FilterService
) else (
    echo.
    echo Compilation failed!
    exit /b 1
)
