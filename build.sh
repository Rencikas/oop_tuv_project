#!/bin/bash
# Build script for Vehicle Management System

echo "Creating build directory..."
mkdir -p bin

echo "Compiling Java files..."
javac -d bin -encoding UTF-8 src/Application.java \
    src/models/*.java \
    src/services/*.java \
    src/persistence/*.java \
    src/util/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "Services available:"
    echo "  - AuthenticationService"
    echo "  - VehicleService"
    echo "  - FilterService"
else
    echo "Compilation failed!"
    exit 1
fi
