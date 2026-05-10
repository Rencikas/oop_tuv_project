#!/usr/bin/env bash

set -euo pipefail

echo
echo "Starting CarInspec UI (macOS / Unix runner)"
echo

if ! command -v java >/dev/null 2>&1; then
  echo "java not found. Install a JDK (e.g., brew install openjdk) and ensure java is on your PATH."
  exit 1
fi

if [ -n "${JAVAFX_SDK:-}" ]; then
  JAVAFX_SDK_DIR="$JAVAFX_SDK"
  PATH_TO_FX="$JAVAFX_SDK_DIR/lib"
elif [ -n "${PATH_TO_FX:-}" ]; then
  PATH_TO_FX="$PATH_TO_FX"
else
  if command -v brew >/dev/null 2>&1; then
    for formula in openjfx openjdk-openjfx; do
      prefix=$(brew --prefix "$formula" 2>/dev/null || true)
      if [ -n "$prefix" ]; then
        candidate="$prefix/libexec/openjfx/lib"
        if [ -d "$candidate" ]; then
          PATH_TO_FX="$candidate"
          break
        fi
        candidate2="$(brew --prefix)/opt/$formula/libexec/openjfx/lib"
        if [ -d "$candidate2" ]; then
          PATH_TO_FX="$candidate2"
          break
        fi
      fi
    done
  fi
fi

if [ -z "${PATH_TO_FX:-}" ] || [ ! -d "$PATH_TO_FX" ]; then
  echo "JavaFX SDK not found. Set JAVAFX_SDK to the SDK root or PATH_TO_FX to the SDK lib dir."
  echo "Example: export JAVAFX_SDK=\"/Users/you/Downloads/javafx-sdk-17.0.8\""
  echo "Or: export PATH_TO_FX=\"/Users/you/Downloads/javafx-sdk-17.0.8/lib\""
  exit 1
fi

echo "Using JavaFX SDK libs from: $PATH_TO_FX"

if [ ! -d bin ] || [ -z "$(ls -A bin 2>/dev/null || true)" ]; then
  if [ -x ./build.sh ]; then
    echo "Backend not compiled or bin/ missing. Running ./build.sh"
    ./build.sh
  else
    echo "Backend not compiled and build.sh not found — compiling backend sources to bin/"
    mkdir -p bin
    if [ -n "${PATH_TO_FX:-}" ]; then
      javac --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -d bin -encoding UTF-8 \
        src/Application.java src/models/*.java src/services/*.java src/persistence/*.java src/util/*.java || {
        echo "Backend compilation failed"
        exit 1
      }
    else
      javac -d bin -encoding UTF-8 \
        src/Application.java src/models/*.java src/services/*.java src/persistence/*.java src/util/*.java || {
        echo "Backend compilation failed"
        exit 1
      }
    fi
  fi
fi

echo
echo "Compiling UI (src/ui/MainApp.java) into bin/"
if [ -n "${PATH_TO_FX:-}" ]; then
  javac --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml -d bin -cp src -encoding UTF-8 src/ui/MainApp.java || {
    echo "UI compilation failed. javac error above."
    exit 1
  }
else
  javac -d bin -cp src -encoding UTF-8 src/ui/MainApp.java || {
    echo "UI compilation failed. javac error above."
    exit 1
  }
fi

echo
echo "Compilation successful. Launching application..."

if [ -d "src/resources" ]; then
  echo "Copying resources into bin/"
  mkdir -p bin/resources
  cp -R src/resources/* bin/ 2>/dev/null || true
fi

java --module-path "$PATH_TO_FX" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics -cp bin ui.MainApp || {
  code=$?
  echo
  echo "Application exited with code: $code"
  exit $code
}
