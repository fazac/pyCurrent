import akshare as ak
import pymysql
import datetime
import argparse
from sqlalchemy import create_engine
from operator import methodcaller

parser = argparse.ArgumentParser()

parser.add_argument('--em_func', type=str, default='stock_zh_a_spot_em')
parser.add_argument('--em_table', type=str, default='em_real_time_stock')
args = parser.parse_args()

em_func = args.em_func
em_table = args.em_table
em_table_tmp = em_table + "_tmp"

engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stock?charset=utf8")
db = pymysql.connect(host='localhost', port=3306, user='root', passwd='123456', db='stock', charset='utf8')
sql1 = "drop table if exists " + em_table_tmp
cursor = db.cursor()
cursor.execute(sql1)
now = datetime.datetime.now()

df = methodcaller(em_func)(ak)
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
