::for /f "tokens=5" %%a in ('netstat /ano ^| findstr 19090') do taskkill /F /pid %%a
taskkill -f -t -im javaw.exe

pause