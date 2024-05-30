import pymysql
import pyecharts
from pyecharts.charts import Line,Bar,Grid
import pyecharts.options as opts
import argparse
import datetime
from sqlalchemy import create_engine

parser = argparse.ArgumentParser()

parser.add_argument('--emcode', type=str, default='300623')
args = parser.parse_args()
code = args.emcode
em_table = "em_real_time_stock_" + datetime.datetime.now().strftime('%Y%m%d')
# 连接到MySQL数据库
engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stockrealtime?charset=utf8")
db = pymysql.connect(host='localhost', port=3306, user='root', passwd='123456', db='stockrealtime', charset='utf8')

cursor = db.cursor()

# 提取数据

sql1 = "select current_pri, vol,ROW_NUMBER() OVER () AS ri  from " + em_table + " where ts_code = '" + code + "' and right(trade_date,8)> '09:29:00';"
cursor.execute(sql1)
data = cursor.fetchall()
# x_data = [row[0] for row in data]
y_data = [row[0] for row in data]
x_data = [row[2] for row in data]

x_data_line=[da-1 for i,da in enumerate(x_data) if i < len(x_data)]

y_date3_tmp = [0] + [row[1] for row in data]
y_date3 = [y_date3_tmp[i+1]- date3 for i,date3 in enumerate(y_date3_tmp) if i < len(y_date3_tmp)-1]

print(len(x_data))
print(len(y_data))
print(len(y_date3))
sql2 = "select bar from real_bar where ts_code = '" + code + "' and trade_date > curdate();"
cursor.execute(sql2)
data2 = cursor.fetchall()
y_data2 = [row[0] for row in data2]

# 画图


bar=(
    Bar(init_opts=opts.InitOpts(width="1366px", height="768px"))
    .add_xaxis(x_data)
    .extend_axis(yaxis=opts.AxisOpts(
                        is_scale=True,
                    )
     )
    .add_yaxis(
         "v",
         y_date3,
         color="#6e9ef1",
         label_opts=opts.LabelOpts(is_show=False),
     )
     .set_series_opts(
        itemstyle_opts=opts.ItemStyleOpts(opacity=0.7)
     )
)


line=(
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "",
        y_data,
        yaxis_index=1,
        itemstyle_opts=opts.ItemStyleOpts(color='red'),
        symbol="emptyCircle",
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=3),
    )
    .set_global_opts(
        title_opts=opts.TitleOpts(title="", subtitle=""),
        yaxis_opts=opts.AxisOpts(is_scale=True),
    )
)

line1=(
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "",
        y_data2,
        is_smooth=True,
        itemstyle_opts=opts.ItemStyleOpts(color='#6e9ef1'),
        symbol="emptyCircle",
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=1),
        areastyle_opts=opts.AreaStyleOpts(opacity=0.05)
    )
    .set_global_opts(
        title_opts=opts.TitleOpts(title="", subtitle=""),
        yaxis_opts=opts.AxisOpts(is_scale=True),
     )
)    

c=bar.overlap(line)
c=bar.overlap(line1)
c.render("line_chart.html")

'''
grid = (
    Grid()
    .add(c,grid_opts=opts.GridOpts(pos_bottom="60%",
                                 ),
        is_control_axis_index=True
     )
     .add(line1,grid_opts=opts.GridOpts(
                                 pos_bottom="10%",
                                 ),
        is_control_axis_index=True
     )
       
)

grid.render("line_chart.html")

'''
# 关闭数据库连接
db.close()
