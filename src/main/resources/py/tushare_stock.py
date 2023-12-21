import pymysql
import datetime
import tushare as ts
import argparse
from sqlalchemy import create_engine

parser = argparse.ArgumentParser()

parser.add_argument('--day', type=int, default='1000')
args = parser.parse_args()
interval = args.day

# 连接 tushare,获取行情数据
pro = ts.pro_api('56da35294028417e16dbedb23b66f615ca765532668da693666c989d')
engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stock?charset=utf8")

# 打开数据库连接
db = pymysql.connect(host='localhost', port=3306, user='root', passwd='123456', db='stock', charset='utf8')

# 获取当前日期
now = datetime.datetime.now()

# 使用 cursor() 方法创建一个游标对象 cursor
cursor = db.cursor()
sql1 = "drop table if exists stock_daily_basic"

for i in range(0, interval):
    delta = datetime.timedelta(days=i)
    n_days = now - delta
    rq = n_days.strftime('%Y%m%d')

    # 清空临时表
    cursor.execute(sql1)

    # （Tushare数据接口）获取数据，然后插入到数据库临时表stock_daily_basic
    df = pro.daily(trade_date=rq)
    df.to_sql(name='stock_daily_basic', con=engine, if_exists='append', index=False)

    # 从临时表插入到目标表
    sql2 = "insert into tu_stock select * from stock_daily_basic"
    cursor.execute(sql2)
    db.commit()

cursor.execute(sql1)
db.commit()
# 关闭数据库连接
db.close()
