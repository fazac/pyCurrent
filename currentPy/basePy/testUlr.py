from selenium import webdriver
import requests
from fake_useragent import UserAgent
import py_mini_racer
import pathlib
from importlib import resources
import execjs


def get_ths_js(file: str = "ths.js") -> pathlib.Path:
    """
    get path to data "ths.js" text file.
    :return: 文件路径
    :rtype: pathlib.Path
    """
    with resources.path(package="akshare.data", resource=file) as f:
        data_file_path = f
        return data_file_path


def _get_file_content_ths(file: str = "ths.js") -> str:
    """
    获取 JS 文件的内容
    :param file:  JS 文件名
    :type file: str
    :return: 文件内容
    :rtype: str
    """
    setting_file_path = get_ths_js(file)
    with open(setting_file_path, encoding="utf-8") as f:
        file_data = f.read()
    return file_data


js_code = py_mini_racer.MiniRacer()
js_content = _get_file_content_ths("ths.js")
js_code.eval(js_content)
v_code = js_code.call("v")
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                  "(KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36",
    "Cookie": f"v={v_code}",
}
print(v_code)

with open('../ths.js', encoding="utf-8") as f:
    js_content = f.read()
vd = execjs.compile(js_content).call('v')
print(vd)
# AhDkFiJUeSxxFh_1e7EM2-414F9i2fQjFr1IJwrh3Gs-RbDvsunEs2bNGLZZ

# # 读取JavaScript文件内容
# with open('tstJs.js', 'r') as f:
#     js_content = f.read()
#
# # 创建JavaScript运行环境
# context = execjs.compile(js_content)
#
# # 调用自执行函数
# context.call()

url = "http://data.10jqka.com.cn/rank/lxsz/field/lxts/order/desc/page/1/ajax/1/free/1/"
r = requests.get(url, timeout=15, params={},
                 headers={"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                                        "(KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36",
                          "Cookie": f"v={vd}"})
print(r.text)
