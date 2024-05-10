::for /f "tokens=5" %%a in ('netstat /ano ^| findstr 19092') do taskkill /F /pid %%a

taskkill -f -t -im pycur.exe

pause