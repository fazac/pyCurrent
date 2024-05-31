import pymysql
import pyecharts
from pyecharts.charts import Line,Bar,Grid
import pyecharts.options as opts
import argparse
import datetime
from sqlalchemy import create_engine
from pyecharts.globals import ThemeType, RenderType

parser = argparse.ArgumentParser()

parser.add_argument('--emcode', type=str, default='300342')
args = parser.parse_args()
code = args.emcode
em_table = "em_real_time_stock_" + datetime.datetime.now().strftime('%Y%m%d')
# 连接到MySQL数据库
engine = create_engine("mysql+pymysql://root:123456@localhost:3306/stockrealtime?charset=utf8")
db = pymysql.connect(host='localhost', port=3306, user='root', passwd='123456', db='stockrealtime', charset='utf8')

cursor = db.cursor()

# 提取数据

sql1 = "select current_pri, vol,ROW_NUMBER() OVER () AS ri,change_hand  from " + em_table + " where ts_code = '" + code + "' and right(trade_date,8)> '09:29:00';"
cursor.execute(sql1)
data = cursor.fetchall()
# x_data = [row[0] for row in data]
y_data = [row[0] for row in data]
x_data = [row[2] for row in data]

y_data4 = [row[3] for row in data]

x_data_line=[da-1 for i,da in enumerate(x_data) if i < len(x_data)]

y_date3_tmp = [0] + [row[1] for row in data]
y_date3 = [y_date3_tmp[i+1]- date3 for i,date3 in enumerate(y_date3_tmp) if i < len(y_date3_tmp)-1]


sql2 = "select bar from real_bar where ts_code = '" + code + "' and trade_date > curdate();"
cursor.execute(sql2)
data2 = cursor.fetchall()
y_data2 = [row[0] for row in data2]

# 画图


bar=(
    Bar(init_opts=opts.InitOpts(width="100vw", height="100vh",theme=ThemeType.CHALK))
    .add_xaxis(x_data)   
    .add_yaxis(
         "v",
         y_date3,
         color="#6e9ef1",
         yaxis_index=1,
         label_opts=opts.LabelOpts(is_show=False),
     )
     .extend_axis(
        yaxis=opts.AxisOpts(                     
            position="left",
            is_show=False,
        )
     )
     .extend_axis(
        yaxis=opts.AxisOpts(
            name="",  # y轴名称
            type_="value",
            is_scale=True,
            position="right",  # 位于y轴右侧
        )
     )
     .extend_axis(
        yaxis=opts.AxisOpts(
            name="",  # y轴名称
            type_="value",
            is_scale=True,
            is_show=False,
            position="right",  # 位于y轴右侧
        )
     )
     .extend_axis(
        yaxis=opts.AxisOpts(
            name="",  # y轴名称
            type_="value",
            is_scale=True,
            is_show=False,
            position="right",  # 位于y轴右侧
        )
     )
     .set_series_opts(
        itemstyle_opts=opts.ItemStyleOpts(opacity=0.7)
     )
     .set_global_opts( 
        yaxis_opts=opts.AxisOpts(),
        xaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
     )
)


line=(
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "",
        y_data,
        yaxis_index=2,
        label_opts=opts.LabelOpts(is_show=False),
        symbol="emptyCircle",
        itemstyle_opts=opts.ItemStyleOpts(color='red'),
        linestyle_opts=opts.LineStyleOpts(width=3),
        markpoint_opts=opts.MarkPointOpts(data=[opts.MarkPointItem(type_="min"),opts.MarkPointItem(type_="max")]),
    )
    .set_global_opts(
        title_opts=opts.TitleOpts(title="", subtitle=""),
        yaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
        xaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
    )
)

line1=(
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "b",
        y_data2,
        is_smooth=True,
        yaxis_index=3,
        itemstyle_opts=opts.ItemStyleOpts(color='#6e9ef1'),
        symbol="emptyCircle",
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=1),
        areastyle_opts=opts.AreaStyleOpts(opacity=0.5)
    )
    .set_global_opts(
        title_opts=opts.TitleOpts(title="", subtitle=""),
        xaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
        yaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
     )
)    

line2=(
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "h",
        y_data4,
        is_smooth=True,
        yaxis_index=4,
        itemstyle_opts=opts.ItemStyleOpts(color='yellow'),
        symbol="emptyCircle",
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=1),
        markpoint_opts=opts.MarkPointOpts(data=[opts.MarkPointItem(type_="max")],symbol="diamond",symbol_size=[8,8],label_opts = opts.LabelOpts(position="bottom", color="yellow")),
    )
    .set_global_opts(
        title_opts=opts.TitleOpts(title="", subtitle=""),
        xaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
        yaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False)),
     )
)    



c=bar.overlap(line1).overlap(line2).overlap(line)

html_file = "line_chart.html"
c.render(html_file)

# 打开并修改模板文件
with open(html_file, 'r') as file:
    content = file.read()

# 向 <head> 标签中添加额外的代码
extra_code_head = """
    <style>
        html,
        body {
            width: 100%;
            height: 100%;
            margin: 0;
            overflow: hidden;
        }
    </style>
"""

head_content = content.replace('</head>',extra_code_head + '</head>')

# 向 <script> 标签中添加额外的代码
extra_code_script = """
window.addEventListener('resize', myChart.resize);
"""

split_content = head_content.rsplit('</script>', maxsplit=1)
script_content = (extra_code_script+'</script>').join(split_content)


# 保存修改后的模板文件
with open(html_file, 'w') as file:
    file.write(script_content)

print('模版文件修改成功!')


# 关闭数据库连接
db.close()
