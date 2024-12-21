import random
import requests
from fake_useragent import UserAgent
from urllib3.exceptions import InsecureRequestWarning


def open_proxy():
    with open('proxy.txt', 'r') as f:
        # 一行一行读取
        ip = [line.strip() for line in f]
    return ip


def get_proxy():
    ip = open_proxy()
    sip = random.choice(ip)
    proxies = {"http": "http://{}".format(sip)}
    return proxies


def get_proxy_url(url, params):
    requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
    headers = {'User-Agent': UserAgent().random}
    proxies = get_proxy()
    print(proxies)
    return requests.get(url, timeout=15, params=params, headers=headers, proxies=proxies, verify=False)
