from tkinter import EXCEPTION

import requests
from fake_useragent import UserAgent
from bs4 import BeautifulSoup
import time
from urllib3.exceptions import InsecureRequestWarning


def open_proxy():
    with open('proxy.txt', 'r') as f:
        # 一行一行读取
        ip = [line.strip() for line in f]
    return ip


def get_text(url, headers):
    # ip = open_proxy()
    # sip = random.choice(ip)
    # print(sip)
    # proxies = {"http": "http://{}".format(sip), "https": "http://{}".format(sip)}
    proxies = {"http": "http://127.0.0.1:7890", "https": "http://127.0.0.1:7890"}
    # s = requests.session()
    # s.keep_alive = False  # 关闭多余连接
    # r = s.get(url, timeout=15, params={}, headers=headers, proxies=proxies)
    requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
    r = requests.get(url, timeout=15, params={}, headers=headers, proxies=proxies, verify=False)
    r.encoding = r.apparent_encoding
    soup = BeautifulSoup(r.text, 'html.parser')
    t_content = soup.find('div', {'class': 'txtnav'})
    print(t_content.find('h1').text)
    t_next_url = soup.find('div', {'class': 'page1'}).find_all('a')[3].get("href")
    filename = "huoying.txt"
    file = open(filename, "a")
    file.write(t_content.text)
    file.close()
    return t_next_url


if __name__ == '__main__':
    url = "https://69shuba.cx/txt/58637/37787324"
    headers = {'User-Agent': UserAgent().random}
    # url = "https://www.google.com"
    # url = "https://www.baidu.com/"
    next_url = get_text(url, headers)
    print(next_url)
    # url_head = "https://alicesw.org"
    while not (next_url.endswith('htm')):
        try:
            time.sleep(3)
            next_url = get_text(next_url, headers)
            print(next_url)
        except:
            pass
    print("FINISH")
