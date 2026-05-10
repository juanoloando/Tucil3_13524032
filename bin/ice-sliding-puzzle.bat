@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
for %%I in ("%SCRIPT_DIR%..") do set "PROJECT_ROOT=%%~fI"

set "APP_JAR=%PROJECT_ROOT%\target\ice-sliding-puzzle-1.0.0.jar"
set "LIB_DIR=%PROJECT_ROOT%\target\lib"

if not exist "%APP_JAR%" (
    echo File jar tidak ditemukan:
    echo   %APP_JAR%
    echo.
    echo Jalankan "mvn clean package" dari root project terlebih dahulu.
    pause
    exit /b 1
)

if not exist "%LIB_DIR%\javafx-controls-21.0.2-win.jar" (
    echo Runtime JavaFX belum ada di:
    echo   %LIB_DIR%
    echo.
    echo Jalankan "mvn clean package" dari root project terlebih dahulu.
    pause
    exit /b 1
)

java --module-path "%LIB_DIR%" --add-modules javafx.controls,javafx.fxml -jar "%APP_JAR%"
set "EXIT_CODE=%ERRORLEVEL%"

if not "%EXIT_CODE%"=="0" (
    echo.
    echo Aplikasi berhenti dengan kode %EXIT_CODE%.
    pause
)

exit /b %EXIT_CODE%
