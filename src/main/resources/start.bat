@echo off
setlocal

set JAVA_HOME=C:\Users\fa\Documents\jdk-21.0.1
set JAR_FILE=C:\Users\fa\.m2\repository\com\stock\pyCurrent\0.0.1-SNAPSHOT\pyCurrent-0.0.1-SNAPSHOT.jar
set LOG_FILE=C:\Users\fa\log\spring.log

echo Starting Java application...

"%JAVA_HOME%\bin\javaw.exe" -jar "%JAR_FILE%" > "%LOG_FILE%" 2>&1

echo Java application has been started.

pause