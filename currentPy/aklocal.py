import pandas as pd
import requests
from fake_useragent import UserAgent
from bs4 import BeautifulSoup
from io import StringIO
from py_mini_racer import MiniRacer


def get_url(url, params):
    return requests.get(url, timeout=15, params=params)


def get_thx_cookie():
    with open('ths.js', encoding="utf-8") as f:
        js_content = f.read()
    racer = MiniRacer()
    racer.eval(js_content)
    return racer.call('v')


def stock_zh_a_spot_em() -> pd.DataFrame:
    """
    东方财富网-沪深京 A 股-实时行情
    https://quote.eastmoney.com/center/gridlist.html#hs_a_board
    :return: 实时行情
    :rtype: pandas.DataFrame
    """
    url = "https://82.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "50000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "fid": "f3",
        "fs": "m:0 t:6,m:0 t:80,m:1 t:2,m:1 t:23,m:0 t:81 s:2048",
        "fields": "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,"
                  "f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152",
        "_": "1623833739532",
    }
    r = get_url(url, params)
    data_json = r.json()
    if not data_json["data"]["diff"]:
        return pd.DataFrame()
    temp_df = pd.DataFrame(data_json["data"]["diff"])
    temp_df.columns = [
        "_",
        "最新价",
        "涨跌幅",
        "涨跌额",
        "成交量",
        "成交额",
        "振幅",
        "换手率",
        "市盈率-动态",
        "量比",
        "5分钟涨跌",
        "代码",
        "_",
        "名称",
        "最高",
        "最低",
        "今开",
        "昨收",
        "总市值",
        "流通市值",
        "涨速",
        "市净率",
        "60日涨跌幅",
        "年初至今涨跌幅",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
    ]
    temp_df.reset_index(inplace=True)
    temp_df["index"] = temp_df.index + 1
    temp_df.rename(columns={"index": "序号"}, inplace=True)
    temp_df = temp_df[
        [
            "序号",
            "代码",
            "名称",
            "最新价",
            "涨跌幅",
            "涨跌额",
            "成交量",
            "成交额",
            "振幅",
            "最高",
            "最低",
            "今开",
            "昨收",
            "量比",
            "换手率",
            "市盈率-动态",
            "市净率",
            "总市值",
            "流通市值",
            "涨速",
            "5分钟涨跌",
            "60日涨跌幅",
            "年初至今涨跌幅",
        ]
    ]
    temp_df["最新价"] = pd.to_numeric(temp_df["最新价"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["成交量"] = pd.to_numeric(temp_df["成交量"], errors="coerce")
    temp_df["成交额"] = pd.to_numeric(temp_df["成交额"], errors="coerce")
    temp_df["振幅"] = pd.to_numeric(temp_df["振幅"], errors="coerce")
    temp_df["最高"] = pd.to_numeric(temp_df["最高"], errors="coerce")
    temp_df["最低"] = pd.to_numeric(temp_df["最低"], errors="coerce")
    temp_df["今开"] = pd.to_numeric(temp_df["今开"], errors="coerce")
    temp_df["昨收"] = pd.to_numeric(temp_df["昨收"], errors="coerce")
    temp_df["量比"] = pd.to_numeric(temp_df["量比"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df["市盈率-动态"] = pd.to_numeric(temp_df["市盈率-动态"], errors="coerce")
    temp_df["市净率"] = pd.to_numeric(temp_df["市净率"], errors="coerce")
    temp_df["总市值"] = pd.to_numeric(temp_df["总市值"], errors="coerce")
    temp_df["流通市值"] = pd.to_numeric(temp_df["流通市值"], errors="coerce")
    temp_df["涨速"] = pd.to_numeric(temp_df["涨速"], errors="coerce")
    temp_df["5分钟涨跌"] = pd.to_numeric(temp_df["5分钟涨跌"], errors="coerce")
    temp_df["60日涨跌幅"] = pd.to_numeric(temp_df["60日涨跌幅"], errors="coerce")
    temp_df["年初至今涨跌幅"] = pd.to_numeric(
        temp_df["年初至今涨跌幅"], errors="coerce"
    )
    return temp_df


def fund_etf_spot_em() -> pd.DataFrame:
    """
    东方财富-ETF 实时行情
    https://quote.eastmoney.com/center/gridlist.html#fund_etf
    :return: ETF 实时行情
    :rtype: pandas.DataFrame
    """
    url = "https://88.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "5000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "wbp2u": "|0|0|0|web",
        "fid": "f3",
        "fs": "b:MK0021,b:MK0022,b:MK0023,b:MK0024",
        "fields": (
            "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,"
            "f12,f13,f14,f15,f16,f17,f18,f20,f21,"
            "f23,f24,f25,f22,f11,f30,f31,f32,f33,"
            "f34,f35,f38,f62,f63,f64,f65,f66,f69,"
            "f72,f75,f78,f81,f84,f87,f115,f124,f128,"
            "f136,f152,f184,f297,f402,f441"
        ),
        "_": "1672806290972",
    }
    r = get_url(url, params)
    data_json = r.json()
    temp_df = pd.DataFrame(data_json["data"]["diff"])
    temp_df.rename(
        columns={
            "f12": "代码",
            "f14": "名称",
            "f2": "最新价",
            "f4": "涨跌额",
            "f3": "涨跌幅",
            "f5": "成交量",
            "f6": "成交额",
            "f7": "振幅",
            "f17": "开盘价",
            "f15": "最高价",
            "f16": "最低价",
            "f18": "昨收",
            "f8": "换手率",
            "f10": "量比",
            "f30": "现手",
            "f31": "买一",
            "f32": "卖一",
            "f33": "委比",
            "f34": "外盘",
            "f35": "内盘",
            "f62": "主力净流入-净额",
            "f184": "主力净流入-净占比",
            "f66": "超大单净流入-净额",
            "f69": "超大单净流入-净占比",
            "f72": "大单净流入-净额",
            "f75": "大单净流入-净占比",
            "f78": "中单净流入-净额",
            "f81": "中单净流入-净占比",
            "f84": "小单净流入-净额",
            "f87": "小单净流入-净占比",
            "f38": "最新份额",
            "f21": "流通市值",
            "f20": "总市值",
            "f402": "基金折价率",
            "f441": "IOPV实时估值",
            "f297": "数据日期",
            "f124": "更新时间",
        },
        inplace=True,
    )
    temp_df = temp_df[
        [
            "代码",
            "名称",
            "最新价",
            "IOPV实时估值",
            "基金折价率",
            "涨跌额",
            "涨跌幅",
            "成交量",
            "成交额",
            "开盘价",
            "最高价",
            "最低价",
            "昨收",
            "振幅",
            "换手率",
            "量比",
            "委比",
            "外盘",
            "内盘",
            "主力净流入-净额",
            "主力净流入-净占比",
            "超大单净流入-净额",
            "超大单净流入-净占比",
            "大单净流入-净额",
            "大单净流入-净占比",
            "中单净流入-净额",
            "中单净流入-净占比",
            "小单净流入-净额",
            "小单净流入-净占比",
            "现手",
            "买一",
            "卖一",
            "最新份额",
            "流通市值",
            "总市值",
            "数据日期",
            "更新时间",
        ]
    ]
    temp_df["最新价"] = pd.to_numeric(temp_df["最新价"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["成交量"] = pd.to_numeric(temp_df["成交量"], errors="coerce")
    temp_df["成交额"] = pd.to_numeric(temp_df["成交额"], errors="coerce")
    temp_df["开盘价"] = pd.to_numeric(temp_df["开盘价"], errors="coerce")
    temp_df["最高价"] = pd.to_numeric(temp_df["最高价"], errors="coerce")
    temp_df["最低价"] = pd.to_numeric(temp_df["最低价"], errors="coerce")
    temp_df["昨收"] = pd.to_numeric(temp_df["昨收"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df["量比"] = pd.to_numeric(temp_df["量比"], errors="coerce")
    temp_df["委比"] = pd.to_numeric(temp_df["委比"], errors="coerce")
    temp_df["外盘"] = pd.to_numeric(temp_df["外盘"], errors="coerce")
    temp_df["内盘"] = pd.to_numeric(temp_df["内盘"], errors="coerce")
    temp_df["流通市值"] = pd.to_numeric(temp_df["流通市值"], errors="coerce")
    temp_df["总市值"] = pd.to_numeric(temp_df["总市值"], errors="coerce")
    temp_df["振幅"] = pd.to_numeric(temp_df["振幅"], errors="coerce")
    temp_df["现手"] = pd.to_numeric(temp_df["现手"], errors="coerce")
    temp_df["买一"] = pd.to_numeric(temp_df["买一"], errors="coerce")
    temp_df["卖一"] = pd.to_numeric(temp_df["卖一"], errors="coerce")
    temp_df["最新份额"] = pd.to_numeric(temp_df["最新份额"], errors="coerce")
    temp_df["IOPV实时估值"] = pd.to_numeric(temp_df["IOPV实时估值"], errors="coerce")
    temp_df["基金折价率"] = pd.to_numeric(temp_df["基金折价率"], errors="coerce")
    temp_df["主力净流入-净额"] = pd.to_numeric(
        temp_df["主力净流入-净额"], errors="coerce"
    )
    temp_df["主力净流入-净占比"] = pd.to_numeric(
        temp_df["主力净流入-净占比"], errors="coerce"
    )
    temp_df["超大单净流入-净额"] = pd.to_numeric(
        temp_df["超大单净流入-净额"], errors="coerce"
    )
    temp_df["超大单净流入-净占比"] = pd.to_numeric(
        temp_df["超大单净流入-净占比"], errors="coerce"
    )
    temp_df["大单净流入-净额"] = pd.to_numeric(
        temp_df["大单净流入-净额"], errors="coerce"
    )
    temp_df["大单净流入-净占比"] = pd.to_numeric(
        temp_df["大单净流入-净占比"], errors="coerce"
    )
    temp_df["中单净流入-净额"] = pd.to_numeric(
        temp_df["中单净流入-净额"], errors="coerce"
    )
    temp_df["中单净流入-净占比"] = pd.to_numeric(
        temp_df["中单净流入-净占比"], errors="coerce"
    )
    temp_df["小单净流入-净额"] = pd.to_numeric(
        temp_df["小单净流入-净额"], errors="coerce"
    )
    temp_df["小单净流入-净占比"] = pd.to_numeric(
        temp_df["小单净流入-净占比"], errors="coerce"
    )
    temp_df["数据日期"] = pd.to_datetime(
        temp_df["数据日期"], format="%Y%m%d", errors="coerce"
    )
    temp_df["更新时间"] = (
        pd.to_datetime(temp_df["更新时间"], unit="s", errors="coerce")
        .dt.tz_localize("UTC")
        .dt.tz_convert("Asia/Shanghai")
    )

    return temp_df


def stock_zh_a_hist(
        symbol: str = "000001",
        period: str = "daily",
        start_date: str = "19700101",
        end_date: str = "20500101",
        adjust: str = "",
        timeout: float = None,
) -> pd.DataFrame:
    """
    东方财富网-行情首页-沪深京 A 股-每日行情
    https://quote.eastmoney.com/concept/sh603777.html?from=classic
    :param symbol: 股票代码
    :type symbol: str
    :param period: choice of {'daily', 'weekly', 'monthly'}
    :type period: str
    :param start_date: 开始日期
    :type start_date: str
    :param end_date: 结束日期
    :type end_date: str
    :param adjust: choice of {"qfq": "前复权", "hfq": "后复权", "": "不复权"}
    :type adjust: str
    :param timeout: choice of None or a positive float number
    :type timeout: float
    :return: 每日行情
    :rtype: pandas.DataFrame
    """
    adjust_dict = {"qfq": "1", "hfq": "2", "": "0"}
    period_dict = {"daily": "101", "weekly": "102", "monthly": "103"}
    url = "https://push2his.eastmoney.com/api/qt/stock/kline/get"
    if not symbol.startswith('6') and not symbol.startswith('0') and not symbol.startswith('3'):
        return pd.DataFrame()
    mid = 1 if symbol.startswith('6') else 0
    params = {
        "fields1": "f1,f2,f3,f4,f5,f6",
        "fields2": "f51,f52,f53,f54,f55,f56,f57,f58,f59,f60,f61,f116",
        "ut": "7eea3edcaed734bea9cbfc24409ed989",
        "klt": period_dict[period],
        "fqt": adjust_dict[adjust],
        "secid": f"{mid}.{symbol}",
        "beg": start_date,
        "end": end_date,
        "_": "1623766962675",
    }
    r = get_url(url, params)
    data_json = r.json()
    if not (data_json["data"] and data_json["data"]["klines"]):
        return pd.DataFrame()
    temp_df = pd.DataFrame([item.split(",") for item in data_json["data"]["klines"]])
    temp_df["股票代码"] = symbol
    temp_df.columns = [
        "日期",
        "开盘",
        "收盘",
        "最高",
        "最低",
        "成交量",
        "成交额",
        "振幅",
        "涨跌幅",
        "涨跌额",
        "换手率",
        "股票代码",
    ]
    temp_df["日期"] = pd.to_datetime(temp_df["日期"], errors="coerce").dt.date
    temp_df["开盘"] = pd.to_numeric(temp_df["开盘"], errors="coerce")
    temp_df["收盘"] = pd.to_numeric(temp_df["收盘"], errors="coerce")
    temp_df["最高"] = pd.to_numeric(temp_df["最高"], errors="coerce")
    temp_df["最低"] = pd.to_numeric(temp_df["最低"], errors="coerce")
    temp_df["成交量"] = pd.to_numeric(temp_df["成交量"], errors="coerce")
    temp_df["成交额"] = pd.to_numeric(temp_df["成交额"], errors="coerce")
    temp_df["振幅"] = pd.to_numeric(temp_df["振幅"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df = temp_df[
        [
            "日期",
            "股票代码",
            "开盘",
            "收盘",
            "最高",
            "最低",
            "成交量",
            "成交额",
            "振幅",
            "涨跌幅",
            "涨跌额",
            "换手率",
        ]
    ]
    return temp_df


def stock_board_cons_em(url, params):
    r = get_url(url, params)
    data_json = r.json()
    temp_df = pd.DataFrame(data_json["data"]["diff"])
    temp_df.reset_index(inplace=True)
    temp_df["index"] = range(1, len(temp_df) + 1)
    temp_df.columns = [
        "序号",
        "_",
        "最新价",
        "涨跌幅",
        "涨跌额",
        "成交量",
        "成交额",
        "振幅",
        "换手率",
        "市盈率-动态",
        "_",
        "_",
        "代码",
        "_",
        "名称",
        "最高",
        "最低",
        "今开",
        "昨收",
        "_",
        "_",
        "_",
        "市净率",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
    ]
    temp_df = temp_df[
        [
            "序号",
            "代码",
            "名称",
            "最新价",
            "涨跌幅",
            "涨跌额",
            "成交量",
            "成交额",
            "振幅",
            "最高",
            "最低",
            "今开",
            "昨收",
            "换手率",
            "市盈率-动态",
            "市净率",
        ]
    ]
    temp_df["最新价"] = pd.to_numeric(temp_df["最新价"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["成交量"] = pd.to_numeric(temp_df["成交量"], errors="coerce")
    temp_df["成交额"] = pd.to_numeric(temp_df["成交额"], errors="coerce")
    temp_df["振幅"] = pd.to_numeric(temp_df["振幅"], errors="coerce")
    temp_df["最高"] = pd.to_numeric(temp_df["最高"], errors="coerce")
    temp_df["最低"] = pd.to_numeric(temp_df["最低"], errors="coerce")
    temp_df["今开"] = pd.to_numeric(temp_df["今开"], errors="coerce")
    temp_df["昨收"] = pd.to_numeric(temp_df["昨收"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df["市盈率-动态"] = pd.to_numeric(temp_df["市盈率-动态"], errors="coerce")
    temp_df["市净率"] = pd.to_numeric(temp_df["市净率"], errors="coerce")
    return temp_df


def stock_board_concept_name_em() -> pd.DataFrame:
    """
    东方财富网-行情中心-沪深京板块-概念板块-名称
    https://quote.eastmoney.com/center/boardlist.html#concept_board
    :return: 概念板块-名称
    :rtype: pandas.DataFrame
    """
    url = "https://79.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "2000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "fid": "f3",
        "fs": "m:90 t:3 f:!50",
        "fields": "f2,f3,f4,f8,f12,f14,f15,f16,f17,f18,f20,f21,f24,f25,f22,f33,f11,f62,f128,f124,f107,f104,f105,f136",
        "_": "1626075887768",
    }
    r = get_url(url, params)
    data_json = r.json()
    temp_df = pd.DataFrame(data_json["data"]["diff"])
    temp_df.reset_index(inplace=True)
    temp_df["index"] = range(1, len(temp_df) + 1)
    temp_df.columns = [
        "排名",
        "最新价",
        "涨跌幅",
        "涨跌额",
        "换手率",
        "_",
        "板块代码",
        "板块名称",
        "_",
        "_",
        "_",
        "_",
        "总市值",
        "_",
        "_",
        "_",
        "_",
        "_",
        "_",
        "上涨家数",
        "下跌家数",
        "_",
        "_",
        "领涨股票",
        "_",
        "_",
        "领涨股票-涨跌幅",
    ]
    temp_df = temp_df[
        [
            "排名",
            "板块名称",
            "板块代码",
            "最新价",
            "涨跌额",
            "涨跌幅",
            "总市值",
            "换手率",
            "上涨家数",
            "下跌家数",
            "领涨股票",
            "领涨股票-涨跌幅",
        ]
    ]
    temp_df["最新价"] = pd.to_numeric(temp_df["最新价"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["总市值"] = pd.to_numeric(temp_df["总市值"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df["上涨家数"] = pd.to_numeric(temp_df["上涨家数"], errors="coerce")
    temp_df["下跌家数"] = pd.to_numeric(temp_df["下跌家数"], errors="coerce")
    temp_df["领涨股票-涨跌幅"] = pd.to_numeric(
        temp_df["领涨股票-涨跌幅"], errors="coerce"
    )
    return temp_df


def stock_board_concept_cons_em(symbol: str = "BK0655") -> pd.DataFrame:
    """
    东方财富-沪深板块-概念板块-板块成份
    https://quote.eastmoney.com/center/boardlist.html#boards-BK06551
    :param symbol: 板块代码
    :type symbol: str
    :return: 板块成份
    :rtype: pandas.DataFrame
    """
    url = "https://29.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "7000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "fid": "f3",
        "fs": f"b:{symbol} f:!50",
        "fields": "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,"
                  "f24,f25,f22,f11,f62,f128,f136,f115,f152,f45",
        "_": "1626081702127",
    }
    return stock_board_cons_em(url, params)


def stock_board_industry_name_em() -> pd.DataFrame:
    """
    东方财富网-沪深板块-行业板块-名称
    https://quote.eastmoney.com/center/boardlist.html#industry_board
    :return: 行业板块-名称
    :rtype: pandas.DataFrame
    """
    url = "https://17.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "2000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "fid": "f3",
        "fs": "m:90 t:2 f:!50",
        "fields": "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,"
                  "f23,f24,f25,f26,f22,f33,f11,f62,f128,f136,f115,f152,f124,f107,f104,f105,"
                  "f140,f141,f207,f208,f209,f222",
        "_": "1626075887768",
    }
    r = get_url(url, params)
    data_json = r.json()
    temp_df = pd.DataFrame(data_json["data"]["diff"])
    temp_df.reset_index(inplace=True)
    temp_df["index"] = temp_df.index + 1
    temp_df.columns = [
        "排名",
        "-",
        "最新价",
        "涨跌幅",
        "涨跌额",
        "-",
        "_",
        "-",
        "换手率",
        "-",
        "-",
        "-",
        "板块代码",
        "-",
        "板块名称",
        "-",
        "-",
        "-",
        "-",
        "总市值",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "-",
        "上涨家数",
        "下跌家数",
        "-",
        "-",
        "-",
        "领涨股票",
        "-",
        "-",
        "领涨股票-涨跌幅",
        "-",
        "-",
        "-",
        "-",
        "-",
    ]
    temp_df = temp_df[
        [
            "排名",
            "板块名称",
            "板块代码",
            "最新价",
            "涨跌额",
            "涨跌幅",
            "总市值",
            "换手率",
            "上涨家数",
            "下跌家数",
            "领涨股票",
            "领涨股票-涨跌幅",
        ]
    ]
    temp_df["最新价"] = pd.to_numeric(temp_df["最新价"], errors="coerce")
    temp_df["涨跌额"] = pd.to_numeric(temp_df["涨跌额"], errors="coerce")
    temp_df["涨跌幅"] = pd.to_numeric(temp_df["涨跌幅"], errors="coerce")
    temp_df["总市值"] = pd.to_numeric(temp_df["总市值"], errors="coerce")
    temp_df["换手率"] = pd.to_numeric(temp_df["换手率"], errors="coerce")
    temp_df["上涨家数"] = pd.to_numeric(temp_df["上涨家数"], errors="coerce")
    temp_df["下跌家数"] = pd.to_numeric(temp_df["下跌家数"], errors="coerce")
    temp_df["领涨股票-涨跌幅"] = pd.to_numeric(
        temp_df["领涨股票-涨跌幅"], errors="coerce"
    )
    return temp_df


def stock_board_industry_cons_em(symbol: str = "BK1036") -> pd.DataFrame:
    """
    东方财富网-沪深板块-行业板块-板块成份
    https://data.eastmoney.com/bkzj/BK1027.html
    :param symbol: 板块代码
    :type symbol: str
    :return: 板块成份
    :rtype: pandas.DataFrame
    """
    url = "http://29.push2.eastmoney.com/api/qt/clist/get"
    params = {
        "pn": "1",
        "pz": "2000",
        "po": "1",
        "np": "1",
        "ut": "bd1d9ddb04089700cf9c27f6f7426281",
        "fltt": "2",
        "invt": "2",
        "fid": "f3",
        "fs": f"b:{symbol} f:!50",
        "fields": "f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,"
                  "f23,f24,f25,f22,f11,f62,f128,f136,f115,f152,f45",
        "_": "1626081702127",
    }
    return stock_board_cons_em(url, params)


def stock_rank_lxsz_ths() -> pd.DataFrame:
    """
    同花顺-数据中心-技术选股-连续上涨
    https://data.10jqka.com.cn/rank/lxsz/
    :return: 连续上涨
    :rtype: pandas.DataFrame
    """
    big_df = pd.DataFrame()
    cookie_v = get_thx_cookie()
    url = "http://data.10jqka.com.cn/rank/lxsz/field/lxts/order/desc/page/1/ajax/1/free/1/"
    r = requests.get(url, timeout=15, params={},
                     headers={'User-Agent': UserAgent().random, "Cookie": f"v={cookie_v}"})
    soup = BeautifulSoup(r.text, features="lxml")
    temp_df = pd.read_html(StringIO(r.text), converters={"股票代码": str})[0]
    big_df = pd.concat(objs=[big_df, temp_df], ignore_index=True)

    try:
        total_page = int(soup.find(name="span", attrs={"class": "page_info"}).text.split(
            "/"
        )[1])
    except AttributeError:
        total_page = 1

    if total_page > 1:
        for page in range(1, total_page + 1):
            url = f"http://data.10jqka.com.cn/rank/lxsz/field/lxts/order/desc/page/{page}/ajax/1/free/1/"
            r = requests.get(url, timeout=15, params={},
                             headers={'User-Agent': UserAgent().random, "Cookie": f"v={cookie_v}"})
            temp_df = pd.read_html(StringIO(r.text), converters={"股票代码": str})[0]
            big_df = pd.concat(objs=[big_df, temp_df], ignore_index=True)
    big_df.columns = [
        "序号",
        "股票代码",
        "股票简称",
        "收盘价",
        "最高价",
        "最低价",
        "连涨天数",
        "连续涨跌幅",
        "累计换手率",
        "所属行业",
    ]
    big_df["连续涨跌幅"] = big_df["连续涨跌幅"].str.strip("%")
    big_df["累计换手率"] = big_df["累计换手率"].str.strip("%")
    big_df["连续涨跌幅"] = pd.to_numeric(big_df["连续涨跌幅"], errors="coerce")
    big_df["累计换手率"] = pd.to_numeric(big_df["累计换手率"], errors="coerce")
    big_df["收盘价"] = pd.to_numeric(big_df["收盘价"], errors="coerce")
    big_df["最高价"] = pd.to_numeric(big_df["最高价"], errors="coerce")
    big_df["最低价"] = pd.to_numeric(big_df["最低价"], errors="coerce")
    big_df["连涨天数"] = pd.to_numeric(big_df["连涨天数"], errors="coerce")
    return big_df
