import requests
from bs4 import BeautifulSoup
from urllib3.exceptions import InsecureRequestWarning


def get_text(url):
    requests.packages.urllib3.disable_warnings(InsecureRequestWarning)
    r = requests.get(url, timeout=15, params={}, verify=False)
    soup = BeautifulSoup(r.text, 'html.parser')
    t_head = soup.find('h3', {'class': 'j_chapterName'})
    t_content = soup.find('div', {'class': 'j_readContent'})
    t_next_url = soup.find('a', {'id': 'j_chapterNext'})
    filename = "xz.txt"
    file = open(filename, "a")
    print(t_head.text)
    file.write(t_head.text)
    file.write("\n")
    for p in t_content.find_all('p'):
        file.write(p.text + ' \n')
    file.close()
    return t_next_url.get("href")


if __name__ == '__main__':
    url = "https://alicesw.org/book/33227/e40169b7c2a79.html"
    next_url = get_text(url)
    url_head = "https://alicesw.org"
    while (next_url.startswith('/book')):
        next_url = get_text(url_head + next_url)
    print("FINISH")
