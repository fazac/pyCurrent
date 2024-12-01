import akshare as ak
import pymysql
import json
import math
import threading
import warnings
import time

from sqlalchemy import create_engine
from operator import methodcaller
from apscheduler.schedulers.background import BackgroundScheduler
from datetime import datetime, timedelta
from dbutils.pooled_db import PooledDB

warnings.simplefilter(action='ignore', category=FutureWarning)

db_config = {
    'host': '192.168.0.12',
    'port': 3306,
    'user': 'root',
    'passwd': '123456',
    'db': 'stockrealtime',
    'charset': 'utf8'
}

pool = PooledDB(creator=pymysql,
                maxcached=40,
                host='192.168.0.12',
                port=3306,
                user='root',
                passwd='123456',
                db='stockrealtime',
                ping=4,
                use_unicode=False,
                charset="utf8",
                maxconnections=40,
                blocking=True, )


def add_one_day(date_str):
    date_obj = datetime.strptime(date_str, '%Y%m%d')
    next_day = date_obj + timedelta(days=1)
    return next_day.strftime('%Y%m%d')


# 多线程获取数据
def fetch_thread_data(em_func, em_table, em_symbol_func, em_symbol_name, em_add_flag, params):
    em_table_tmp = em_table + "_tmp"

    if params is None or params == '':
        em_params = {}
    else:
        em_params = json.loads(params)

    engine = create_engine("mysql+pymysql://root:123456@192.168.0.12:3306/stockrealtime?charset=utf8")

    # symbol_df = ak.stock_zh_a_spot_em()
    symbol_df = methodcaller(em_symbol_func)(ak)
    print(symbol_df)
    sql1 = "drop table if exists " + em_table_tmp

    now = datetime.now()

    class SDateThread(threading.Thread):
        def __init__(self, symbol, s_count):
            super(SDateThread, self).__init__()
            self.symbol = symbol
            self.s_count = str(s_count)

        def run(self):
            conn = pool.connection()
            cursor = conn.cursor()
            try:
                em_params["symbol"] = self.symbol
                df = methodcaller(em_func, **em_params)(ak)
                cursor.execute(sql1 + "_" + self.s_count)
                if df.empty:
                    return
                if '日期' in df:
                    # 日周月行情
                    df.insert(loc=0, column='序号', value=self.symbol)
                    # 日期栏去除'-'
                    df['日期'] = df['日期'].astype(str).str.replace("-", "")
                    df = df.drop('股票代码', axis=1)
                    if em_add_flag == 'yes':
                        df.insert(loc=df.columns.size, column='周期', value=em_params["period"])
                        df.insert(loc=df.columns.size, column='复权', value=em_params["adjust"])
                else:
                    # 板块详情
                    idx = df.columns
                    if '序号' not in idx:
                        df.insert(loc=0, column='序号', value=1)
                    df.loc[df["序号"] > 0, "序号"] = now
                    df.insert(loc=1, column='板块名', value=self.symbol)
                df.to_sql(name=em_table_tmp + "_" + self.s_count, con=engine, if_exists='append', index=False)
                sql2 = "insert into " + em_table + " select * from " + em_table_tmp + "_" + self.s_count
                cursor.execute(sql2)
                conn.commit()

            finally:
                # 无论插入成功还是失败,记得将连接放回连接池供其他线程使用,否则该线程会一直被占用
                cursor.close()
                conn.close()  # 执行完sql操作后,将连接放回连接池,而不是真的关闭连接.如果不放回连接池,则该连接一直处于占用状态,其他线程就无法使用该连接

    # 最多创建10个线程并发执行
    # time.time()
    thread_list = []

    max_row = symbol_df.shape[0]
    t_count = 1
    max_row = math.ceil(max_row / 10)
    for m_symbol in symbol_df[em_symbol_name]:
        thread_list.append(SDateThread(m_symbol, len(thread_list)))
        if len(thread_list) >= 10 or t_count >= max_row:
            # if len(thread_list) >= 1:
            for thread in thread_list:
                thread.start()
            for thread in thread_list:
                thread.join()

            thread_list = []
            t_count = t_count + 1
            # break

    final_conn = pool.connection()
    final_cursor = final_conn.cursor()
    for i in range(0, 10):
        final_cursor.execute(sql1 + "_" + str(i))
    final_cursor.close()
    final_conn.close()


# 单线程获取数据
def fetch_data(em_table, em_func):
    em_table_tmp = em_table + "_tmp"
    engine = create_engine("mysql+pymysql://root:123456@192.168.0.12:3306/stockrealtime?charset=utf8")
    db = pymysql.connect(**db_config)
    sql1 = "drop table if exists " + em_table_tmp
    cursor = db.cursor()
    cursor.execute(sql1)
    now = datetime.now()

    df = methodcaller(em_func)(ak)
    print(df)
    idx = df.columns
    if '序号' not in idx:
        df.insert(loc=0, column='序号', value=1)
    df.loc[df["序号"] > 0, "序号"] = now
    if '日期' in idx:
        df['日期'] = df['日期'].astype(str).str.replace("-", "")
    df.to_sql(name=em_table_tmp, con=engine, if_exists='append', index=False)

    sql2 = "insert into " + em_table + " select * from " + em_table_tmp
    cursor.execute(sql2)
    cursor.execute(sql1)

    db.commit()
    db.close()


def print_hello_log():
    print('hello ' + datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
    filename = "output.txt"
    file = open(filename, "a")
    file.write('hello ' + datetime.now().strftime('%Y-%m-%d %H:%M:%S') + '\n')
    file.close()


def create_rt_table():
    db = pymysql.connect(**db_config)
    cursor = db.cursor()
    em_table = "em_real_time_stock" + "_" + datetime.now().strftime('%Y%m%d')
    sql = ' CREATE TABLE IF NOT EXISTS' + '`' + em_table + '`' + """
   (
        `trade_date`                   varchar(32)    DEFAULT NULL COMMENT '交易日期',
        `ts_code`                      varchar(10)    DEFAULT NULL COMMENT '股票代码',
        `name`                         varchar(8)     DEFAULT NULL COMMENT '名称',
        `current_pri`                  decimal(18, 2) DEFAULT NULL COMMENT '最新价',
        `pct_chg`                      decimal(18, 2) DEFAULT NULL COMMENT '涨跌幅',
        `am_chg`                       decimal(18, 2) DEFAULT NULL COMMENT '涨跌额',
        `vol`                          int            DEFAULT NULL COMMENT '成交量（手）',
        `amount`                       decimal(18, 2) DEFAULT NULL COMMENT '成交额（千元）',
        `vibration`                    decimal(18, 2) DEFAULT NULL COMMENT '振幅',
        `pri_high`                     decimal(18, 2) DEFAULT NULL COMMENT '最高价',
        `pri_low`                      decimal(18, 2) DEFAULT NULL COMMENT '最低价',
        `pri_open`                     decimal(18, 2) DEFAULT NULL COMMENT '开盘价',
        `pri_close_pre`                decimal(18, 2) DEFAULT NULL COMMENT '昨收价',
        `vol_ratio`                    decimal(18, 2) DEFAULT NULL COMMENT '量比',
        `change_hand`                  decimal(18, 2) DEFAULT NULL COMMENT '换手率',
        `pe`                           decimal(18, 2) DEFAULT NULL COMMENT '市盈率(动)',
        `pb`                           decimal(18, 2) DEFAULT NULL COMMENT '市净率',
        `market_cap`                   decimal(18, 2) DEFAULT NULL COMMENT '总市值',
        `circulation_market_cap`       decimal(18, 2) DEFAULT NULL COMMENT '流通市值',
        `increase_ratio`               decimal(18, 2) DEFAULT NULL COMMENT '涨速',
        `five_minutes_increase_ratio`  decimal(18, 2) DEFAULT NULL COMMENT '5分钟涨速',
        `sixty_minutes_increase_ratio` decimal(18, 2) DEFAULT NULL COMMENT '60分钟涨速',
        `current_year_ratio`           decimal(18, 2) DEFAULT NULL COMMENT '年初至今涨跌幅',
        KEY `idx_sdl_code` (`ts_code`) USING BTREE,
        KEY `idx_sdl_date` (`trade_date`) USING BTREE
    ) ENGINE = InnoDB
      DEFAULT CHARSET = utf8mb4
      COLLATE = utf8mb4_general_ci
      ROW_FORMAT = DYNAMIC;
    """
    cursor.execute(sql)
    db.commit()
    db.close()


def fetch_rt_data():
    em_table = "em_real_time_stock" + "_" + datetime.now().strftime('%Y%m%d')
    fetch_data(em_table, "stock_zh_a_spot_em")


def fetch_dn_data():
    db = pymysql.connect(**db_config)
    cursor = db.cursor()
    sql = 'select max(c.trade_date) from em_d_n_stock c'
    cursor.execute(sql)
    max_trade_date = cursor.fetchall()[0][0]
    next_trade_date = add_one_day(max_trade_date)
    now_trade_date = datetime.now().strftime('%Y%m%d')
    if max_trade_date != now_trade_date:
        fetch_thread_data("stock_zh_a_hist",
                          'em_d_n_stock',
                          'stock_zh_a_spot_em',
                          '代码',
                          '',
                          '{\"start_date\":\"' + next_trade_date
                          + '\",\"end_date\":\"' + now_trade_date
                          + '\",\"adjust\":\"\",\"period\":\"daily\"}')


def fetch_da_data():
    db = pymysql.connect(**db_config)
    cursor = db.cursor()
    sql = 'select max(c.trade_date) from em_d_a_stock c'
    cursor.execute(sql)
    max_trade_date = cursor.fetchall()[0][0]
    next_trade_date = add_one_day(max_trade_date)
    now_trade_date = datetime.now().strftime('%Y%m%d')
    if max_trade_date != now_trade_date:
        fetch_thread_data("stock_zh_a_hist",
                          'em_d_a_stock',
                          'stock_zh_a_spot_em',
                          '代码',
                          '',
                          '{\"start_date\":\"' + next_trade_date
                          + '\",\"end_date\":\"' + now_trade_date
                          + '\",\"adjust\":\"hfq\",\"period\":\"daily\"}')


def fetch_concept_data():
    db = pymysql.connect(**db_config)
    cursor = db.cursor()
    sql = 'select max(trade_date) from board_concept_con;'
    cursor.execute(sql)
    max_trade_date = datetime.strptime(cursor.fetchall()[0][0], '%Y-%m-%d %H:%M:%S').strftime('%Y%m%d')
    now_trade_date = datetime.now().strftime('%Y%m%d')
    if max_trade_date != now_trade_date:
        fetch_thread_data("stock_board_concept_cons_em",
                          "board_concept_con",
                          "stock_board_concept_name_em",
                          "板块名称",
                          "",
                          "")


def fetch_industry_data():
    db = pymysql.connect(**db_config)
    cursor = db.cursor()
    sql = 'select max(trade_date) from board_industry_con;'
    cursor.execute(sql)
    max_trade_date = datetime.strptime(cursor.fetchall()[0][0], '%Y-%m-%d %H:%M:%S').strftime('%Y%m%d')
    now_trade_date = datetime.now().strftime('%Y%m%d')
    if max_trade_date != now_trade_date:
        fetch_thread_data("stock_board_industry_cons_em",
                          "board_industry_con",
                          "stock_board_industry_name_em",
                          "板块名称",
                          "",
                          "")


def fetch_continues_data():
    fetch_data("continuous_up", "stock_rank_lxsz_ths")


if __name__ == '__main__':
    print('start frpcpy')
    print(datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
    create_rt_table()
    print('table created')
    task = BackgroundScheduler()

    task.add_job(create_rt_table, 'cron', day_of_week='0-4', hour='9')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='9', minute='14-59/1')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='10', minute='*/1')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='11', minute='0-32/1')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='13', minute='*/1')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='14', minute='*/1')
    task.add_job(fetch_rt_data, 'cron', day_of_week='0-4', hour='15', minute='0-2/1')
    task.add_job(fetch_dn_data, 'cron', day_of_week='0-4', hour='16', minute='0')
    task.add_job(fetch_da_data, 'cron', day_of_week='0-4', hour='16', minute='10')
    task.add_job(fetch_concept_data, 'cron', day_of_week='0-4', hour='16', minute='20')
    task.add_job(fetch_industry_data, 'cron', day_of_week='0-4', hour='16', minute='25')
    task.add_job(fetch_continues_data, 'cron', day_of_week='0-4', hour='16', minute='30')

    # task.add_job(print_hello_log, 'interval', id='5s_job', seconds=5)
    task.start()

    while True:
        time.sleep(60)
