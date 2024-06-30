## 1. install requirement
```
pip install -r .\requirements.txt
```
## 2. pack exe
```
  pyinstaller -F .\akshare_stock_realtime.py --add-data "C:\Users\fa\PycharmProjects\currentPy\venv\Lib\sit
e-packages\py_mini_racer\mini_racer.dll;." --add-data "C:\Users\fa\PycharmProjects\currentPy\venv\Lib\site-packages\akshare\file_fold\calendar.json;akshare\file_fold\"

```

## 3. nssm install service