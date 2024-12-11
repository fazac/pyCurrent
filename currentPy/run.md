## 1. install requirement

```
pip install -r .\requirements.txt
```

## 2. pack exe

```angular2html
pyinstaller --collect-all tzdata  -F .\akshare_stock_realtime.py --add-data "C:\Users\fa\PycharmProjects\currentPy\venv\Lib\site-packages\py_mini_racer\mini_racer.dll;." --add-data "C:\Users\fa\PycharmProjects\currentPy\venv\Lib\site-packages\akshare\file_fold\calendar.json;akshare\file_fold\" --add-data "C:\Users\fa\PycharmProjects\currentPy\venv\Lib\site-packages\akshare\data\ths.js;akshare\data\"
```

## 3. ~~nssm install service~~

```angular2html
in admin powerShell
./nssm.exe install serviceName
./nssm.exe start serviceName
./nssm.exe stop serviceName
./nssm.exe restart serviceName
./nssm.exe remove serviceName
```

## 4. winsw  install service
```angular2html
in admin powerShell
.\WinSW-x64.exe install xx.xml
.\WinSW-x64.exe uninstall xx.xml
.\WinSW-x64.exe start xx.xml
.\WinSW-x64.exe stop xx.xml
.\WinSW-x64.exe restart xx.xml
.\WinSW-x64.exe status xx.xml
```

## 5. docker 
```angular2html
docker build -t pybase .
docker build -t pycpy .
```