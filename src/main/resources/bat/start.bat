@echo off

setlocal
set JAVA_HOME=C:\Users\fa\Documents\jdk-21.0.1

set PACKAGE=C:\Users\fa\Desktop\bat

set JAR_FILE_1=C:\Users\fa\Desktop\bat\pyCurrent-0.0.1-SNAPSHOT.jar
set JAR_SOURCE_FILE_1=C:\Users\fa\.m2\repository\com\stock\pyCurrent\0.0.1-SNAPSHOT\pyCurrent-0.0.1-SNAPSHOT.jar

set JAR_FILE_2=C:\Users\fa\Desktop\bat\pyCurrent-0.0.2-SNAPSHOT.jar
set JAR_SOURCE_FILE_2=C:\Users\fa\.m2\repository\com\stock\pyCurrent\0.0.2-SNAPSHOT\pyCurrent-0.0.1-SNAPSHOT.jar


echo AVAILABLE CHOICE
echo.
echo 0. start all
echo 1. start current task
echo 2. start full task
echo 3. restart current task
echo 4. restart full task
echo 5. stop current task
echo 6. stop full task
echo 7. stop all

echo.
set /p input="please choose next action : "
 
if "%input%"=="0" (
    call :startcur
    call :startfull
) else if "%input%"=="1" (
    call :startcur
) else if "%input%"=="2" (
    call :startfull
) else if "%input%" =="3" (
    echo Restart Current Task...  

    call :stopcur
    call :movejar %JAR_SOURCE_FILE_1%
    call :startcur

    echo Current task has been restarted.
) else if "%input%" =="4" (
    echo Restart Full Task...  

    call :stopfull
    call :movejar %JAR_SOURCE_FILE_2%
    call :startfull

    echo Full task has been restarted.
) else if "%input%"=="5" (
    call :stopcur
) else if "%input%"=="6" (
    call :stopfull
) else if "%input%"=="7" (
    call :stopcur
    call :stopfull
) else (
    echo INVALID CHOICE
)

exit /b 0

:startcur
	start "pycur" "%JAVA_HOME%\bin\pycur.exe" -jar "%JAR_FILE_1%"
	echo pycur task start
goto:eof

:startfull
	start "pyfull" "%JAVA_HOME%\bin\pyfull.exe" -jar "%JAR_FILE_2%"
	echo pyfull task start
goto:eof

:stopcur
	taskkill -f -t -im pycur.exe
	echo pycur process killed
goto:eof

:stopfull
	taskkill -f -t -im pyfull.exe
	echo pyfull process killed
goto:eof
 
:movejar
	copy %~1 %PACKAGE% /y
	echo jar moved
goto:eof

pause


