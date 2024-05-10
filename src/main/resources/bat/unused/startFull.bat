@echo off
setlocal

set JAVA_HOME=C:\Users\fa\Documents\programs\Payara\jdk
set JAR_FILE=C:\Users\fa\.m2\repository\com\example\stockdemo\0.0.1-SNAPSHOT\stockdemo-0.0.1-SNAPSHOT-exec.jar
::set LOG_FILE=C:\Users\fa\Desktop\log\full\spring.log

echo Starting Java application...

start "full" "%JAVA_HOME%\bin\javaw.exe" -jar -Xms12288m  "%JAR_FILE%"

echo Java application has been started.
