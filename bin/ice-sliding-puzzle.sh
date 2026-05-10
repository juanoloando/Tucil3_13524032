#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

APP_JAR="${PROJECT_ROOT}/target/ice-sliding-puzzle-1.0.0.jar"
LIB_DIR="${PROJECT_ROOT}/target/lib"

if [[ ! -f "${APP_JAR}" ]]; then
  echo "File jar tidak ditemukan:"
  echo "  ${APP_JAR}"
  echo
  echo 'Jalankan "mvn clean package" dari root project terlebih dahulu.'
  exit 1
fi

if [[ ! -d "${LIB_DIR}" ]]; then
  echo "Folder runtime JavaFX tidak ditemukan:"
  echo "  ${LIB_DIR}"
  echo
  echo 'Jalankan "mvn clean package" dari root project terlebih dahulu.'
  exit 1
fi

java --module-path "${LIB_DIR}" --add-modules javafx.controls,javafx.fxml -jar "${APP_JAR}"
