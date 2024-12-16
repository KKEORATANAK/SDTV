@REM @echo off
@REM REM Start the POS application
@REM java -jar pos-system.jar

@REM REM Check if the application started successfully
@REM if %errorlevel%==0 (
@REM     REM Wait for a few seconds to ensure the server starts
@REM     timeout /t 5 /nobreak >nul
    
@REM     REM Open Chrome and route to the specified URL
@REM     start chrome.exe https://youtube.com
@REM ) else (
@REM     REM Print an error message if the application fails to start
@REM     echo Failed to start the POS application.
@REM     pause
@REM )


@echo off
REM Start MySQL as a background process
start "" "C:\xampp\mysql\bin\mysqld.exe" --defaults-file="C:\xampp\mysql\bin\my.ini"

REM Wait for a few seconds to ensure MySQL starts
timeout /t 5 /nobreak >nul

REM Start the Java application
start "" java -jar your_application.jar

REM Wait for the Java application to initialize
timeout /t 5 /nobreak >nul

REM Open Google Chrome with a specific URL
start "" "C:\Program Files\Google\Chrome\Application\chrome.exe" http://localhost:8080
