import akshare as ak
import pymysql
import datetime
import argparse
from sqlalchemy import create_engine
from operator import methodcaller
import json

parser = argparse.ArgumentParser()

parser.add_argument('--em_func', type=str, default='stock_sector_fund_flow_rank')
parser.add_argument('--em_table', type=str, default='fund_flow_rank')
parser.add_argument('--em_add_flag', type=str, default='yes')
parser.add_argument('--params', type=str, default='')
parser.add_argument('--now_time', type=str, default='')
args = parser.parse_args()

em_func = args.em_func
em_table = args.em_table
em_table_tmp = em_table + "_tmp"
em_add_flag = args.em_add_flag
em_now_time = args.now_time

if args.params is None or args.params == '':
    em_params = {}
else:
    em_params = json.loads(args.params)

if em_now_time is None or em_now_time == '':
    em_now_time = datetime.datetime.now()

engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stock?charset=utf8")
db = pymysql.connect(host='localhost', port=3306, user='root', passwd='123456', db='stock', charset='utf8')
sql1 = "drop table if exists " + em_table_tmp
cursor = db.cursor()
cursor.execute(sql1)

df = methodcaller(em_func, **em_params)(ak)

idx = df.columns
print(idx)
if '序号' not in idx:
    df.insert(loc=0, column='序号', value=1)
df.loc[df["序号"] > 0, "序号"] = em_now_time
if '日期' in idx:
    df['日期'] = df['日期'].astype(str).str.replace("-", "")
if em_add_flag == 'yes':
    for key in em_params:
        df.insert(loc=df.columns.size, column=key, value=em_params[key])
print(df.columns)
df.to_sql(name=em_table_tmp, con=engine, if_exists='append', index=False)

sql2 = "insert into " + em_table + " select * from " + em_table_tmp
cursor.execute(sql2)
cursor.execute(sql1)

db.commit()
db.close()
