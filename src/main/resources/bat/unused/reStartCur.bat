@echo off
setlocal

set JAVA_HOME=C:\Users\fa\Documents\jdk-21.0.1
set JAR_FILE=C:\Users\fa\Desktop\bat\pyCurrent-0.0.1-SNAPSHOT.jar
set JAR_SOURCE_FILE=C:\Users\fa\.m2\repository\com\stock\pyCurrent\0.0.1-SNAPSHOT\pyCurrent-0.0.1-SNAPSHOT.jar
::set LOG_FILE=C:\Users\fa\Desktop\log\current\spring.log
set PACKAGE=C:\Users\fa\Desktop\bat

echo Restart Java application...

::for /f "tokens=5" %%a in ('netstat /ano ^| findstr 19092') do taskkill /F /pid %%a

::taskkill -f -t -im javaw.exe

taskkill -f -t -im pycur.exe

echo kill javaw

copy %JAR_SOURCE_FILE% %PACKAGE% /y

echo move jar

::"%JAVA_HOME%\bin\javaw.exe" -jar "%JAR_FILE%" >> "%LOG_FILE%" 2>&1

start "pycur" "%JAVA_HOME%\bin\pycur.exe" -jar "%JAR_FILE%"

echo Java application has been started.

