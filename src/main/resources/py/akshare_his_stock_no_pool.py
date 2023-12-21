import akshare as ak
import pymysql
import argparse
import json
import threading
from sqlalchemy import create_engine
from operator import methodcaller
from dbutils.pooled_db import PooledDB
import time
import datetime
import math

# import ast

parser = argparse.ArgumentParser()

parser.add_argument('--em_func', type=str, default='stock_zh_a_hist')
parser.add_argument('--em_table', type=str, default='em_basic_stock')
parser.add_argument('--em_symbol_func', type=str, default='stock_zh_a_spot_em')
parser.add_argument('--em_symbol_name', type=str, default='代码')
parser.add_argument('--em_add_flag', type=str, default='')
parser.add_argument('--params', type=str, default='')
args = parser.parse_args()

em_func = args.em_func
em_symbol_func = args.em_symbol_func
em_symbol_name = args.em_symbol_name
em_add_flag = args.em_add_flag
em_table = args.em_table
em_table_tmp = em_table + "_tmp"

print(args.params)

if args.params is None or args.params == '':
    em_params = {}
else:
    em_params = json.loads(args.params)
# print(type(em_params))
# em_params = ast.literal_eval(args.params)
# print(type(em_params))

engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stock?charset=utf8")
pool = PooledDB(creator=pymysql,
                maxcached=40,
                host='localhost',
                port=3306,
                user='root',
                passwd='123456',
                db='stock',
                ping=4,
                use_unicode=False,
                charset="utf8",
                maxconnections=40,
                blocking=True,
                cursorclass=pymysql.cursors.DictCursor)

# symbol_df = ak.stock_zh_a_spot_em()
symbol_df = methodcaller(em_symbol_func)(ak)
sql1 = "drop table if exists " + em_table_tmp

now = datetime.datetime.now()

start_time = time.time()

print(symbol_df.shape[0])
for m_symbol in symbol_df[em_symbol_name]:
    conn = pool.connection()
    cursor = conn.cursor()
    try:
        em_params["symbol"] = m_symbol
        df = methodcaller(em_func, **em_params)(ak)
        cursor.execute(sql1)
        if not df.empty:
            continue
        if '日期' in df:
            # 日周月行情
            df.insert(loc=0, column='序号', value=m_symbol)
            # 日期栏去除'-'
            df['日期'] = df['日期'].str.replace("-", "")
            if em_add_flag == 'yes':
                df.insert(loc=df.columns.size, column='周期', value=em_params["period"])
                df.insert(loc=df.columns.size, column='复权', value=em_params["adjust"])
        else:
            # 板块详情
            idx = df.columns
            if '序号' not in idx:
                df.insert(loc=0, column='序号', value=1)
            df.loc[df["序号"] > 0, "序号"] = now
            df.insert(loc=1, column='板块名', value=m_symbol)
        df.to_sql(name=em_table_tmp , con=engine, if_exists='append', index=False)
        sql2 = "insert into " + em_table + " select * from " + em_table_tmp
        cursor.execute(sql2)
        conn.commit()
    finally:
        # 无论插入成功还是失败,记得将连接放回连接池供其他线程使用,否则该线程会一直被占用
        cursor.close()
        conn.close()  # 执行完sql操作后,将连接放回连接池,而不是真的关闭连接.如果不放回连接池,则该连接一直处于占用状态,其他线程就无法使用该连接

print("总共用时:" + str(time.time() - start_time).split('.')[0])
