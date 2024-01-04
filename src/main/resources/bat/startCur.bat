@echo off
setlocal

set JAVA_HOME=C:\Users\fa\Documents\jdk-21.0.1
set JAR_FILE=C:\Users\fa\Desktop\bat\pyCurrent-0.0.1-SNAPSHOT.jar
::set LOG_FILE=C:\Users\fa\Desktop\log\current\spring.log

echo Starting Java application...

start "pycur.exe" "%JAVA_HOME%\bin\pycur.exe" -jar "%JAR_FILE%"

echo Java application has been started.

exit