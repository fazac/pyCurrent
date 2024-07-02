@echo off

setlocal
set JAVA_HOME=C:\Users\fa\Documents\jdk-21.0.1

set PACKAGE=C:\Users\fa\Desktop\bat

set JAR_FILE_1=C:\Users\fa\Desktop\bat\pyCurrent-0.0.1-SNAPSHOT.jar
set JAR_SOURCE_FILE_1=C:\Users\fa\.m2\repository\com\stock\pyCurrent\0.0.1-SNAPSHOT\pyCurrent-0.0.1-SNAPSHOT.jar



taskkill -f -t -im pycur.exe
copy %JAR_SOURCE_FILE_1% %PACKAGE% /y
start "pycur" "%JAVA_HOME%\bin\pycur.exe" -jar "%JAR_FILE_1%"

echo pycur restart success

pause