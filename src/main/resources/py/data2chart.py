import pymysql
import pyecharts
from pyecharts.charts import Line, Bar, Grid
import pyecharts.options as opts
import argparse
import datetime
import math
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

sql1 = "select current_pri, vol,ROW_NUMBER() OVER () AS ri,change_hand,pct_chg,pri_close_pre,round(amount/vol/100,2) as ap  from " + em_table + " where ts_code = '" + code + "' and right(trade_date,8)> '09:29:00';"
cursor.execute(sql1)
data = cursor.fetchall()
# x_data = [row[0] for row in data]
y_data = [row[0] for row in data]
x_data = [row[2] for row in data]

y_data4 = [row[3] for row in data]
y_data5 = [row[4] for row in data]

y_data6 = [row[5] for row in data]
y_data7 = [row[6] for row in data]

x_data_line = [da - 1 for i, da in enumerate(x_data) if i < len(x_data)]

y_date3_tmp = [0] + [row[1] for row in data]
y_date3 = [y_date3_tmp[i + 1] - date3 for i, date3 in enumerate(y_date3_tmp) if i < len(y_date3_tmp) - 1]

sql2 = "select bar from real_bar where ts_code = '" + code + "' and trade_date > curdate();"
cursor.execute(sql2)
data2 = cursor.fetchall()
y_data2 = [row[0] for row in data2]

x_max = max(x_data_line)
y_x_max = y_data[x_max]

yf_x_max = y_data5[x_max]

x_min = min(x_data_line)
y_x_min = y_data[x_min]

yf_x_min = y_data5[x_min]

y_pre = y_data6[0]
y_avg = y_data7[len(y_data7) - 1]

y_avg_ratio = round((y_avg - y_pre) * 100 / y_pre, 2)

hand_max = y_data4[x_max]

hand_step = math.ceil(hand_max / 4)

# 画图


bar = (
    Bar(init_opts=opts.InitOpts(width="100vw", height="100vh", theme=ThemeType.CHALK))
    .add_xaxis(
        x_data,
    )
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
            name="p",
            type_="value",
            is_scale=True,
            position="left",
            splitline_opts=opts.SplitLineOpts(is_show=True, linestyle_opts=opts.LineStyleOpts(opacity=0.2)),
        )
    )
    .extend_axis(
        yaxis=opts.AxisOpts(
            name="b",
            type_="value",
            is_scale=True,
            is_show=False,
            position="right",
        )
    )
    .extend_axis(
        yaxis=opts.AxisOpts(
            name="h",
            type_="value",
            is_scale=True,
            is_show=False,
            position="right",
        )
    )
    .extend_axis(
        yaxis=opts.AxisOpts(
            name="g",
            type_="value",
            is_scale=True,
            position="right",
            splitline_opts=opts.SplitLineOpts(is_show=True, linestyle_opts=opts.LineStyleOpts(opacity=0.2)),
        )
    )
    .set_series_opts(
        itemstyle_opts=opts.ItemStyleOpts(opacity=0.3)
    )
    .set_global_opts(
        xaxis_opts=opts.AxisOpts(splitline_opts=opts.SplitLineOpts(is_show=False), is_show=False),
        # title_opts=opts.TitleOpts(title=code[-4:], pos_left="3%", pos_top="46%"),
        legend_opts=opts.LegendOpts(selected_map={"v": True, "p": False, "b": True, "h": True, "g": False}),
        datazoom_opts=opts.DataZoomOpts(type_="inside", range_start=0, range_end=100),
    )
)

line = (
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "p",
        y_data,
        is_smooth=True,
        yaxis_index=2,
        label_opts=opts.LabelOpts(is_show=False),
        symbol="emptyCircle",
        itemstyle_opts=opts.ItemStyleOpts(color='red'),
        linestyle_opts=opts.LineStyleOpts(width=3),
        markpoint_opts=opts.MarkPointOpts(data=[opts.MarkPointItem(type_="max", symbol_size=[18, 18],
                                                                   itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                                                     opacity=0.3)),
                                                opts.MarkPointItem(type_="min", symbol_size=[18, 18],
                                                                   itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                                                     opacity=0.3)),
                                                opts.MarkPointItem(symbol_size=[18, 18],
                                                                   coord=[x_max, y_x_max], value=y_x_max,
                                                                   itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                                                     opacity=0.3)),
                                                opts.MarkPointItem(symbol_size=[18, 18],
                                                                   coord=[x_min, y_x_min], value=y_x_min,
                                                                   itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                                                     opacity=0.3)),
                                                ]
                                          , label_opts=opts.LabelOpts(position="right", color="red", font_weight="bold",
                                                                      font_size=18, background_color="white",
                                                                      padding=[3, 4])
                                          ),
        markline_opts=opts.MarkLineOpts(
            data=[
                opts.MarkLineItem(
                    name="pre",
                    y=y_pre,
                ),
                opts.MarkLineItem(
                    name="avg",
                    y=y_avg,

                )],
            symbol="none",
            linestyle_opts=opts.LineStyleOpts(width=1, color='red', opacity=0.8, type_="dashed")
        ),
    )
)

line1 = (
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "b",
        y_data2,
        is_smooth=True,
        yaxis_index=3,
        itemstyle_opts=opts.ItemStyleOpts(color='#6e9ef1'),
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=1),
        areastyle_opts=opts.AreaStyleOpts(opacity=0.2)
    )
)

line2 = (
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "h",
        y_data4,
        is_smooth=True,
        yaxis_index=4,
        itemstyle_opts=opts.ItemStyleOpts(color='yellow'),
        is_symbol_show=False,
        linestyle_opts=opts.LineStyleOpts(width=1),
        markpoint_opts=opts.MarkPointOpts(
            data=[opts.MarkPointItem(type_="max", itemstyle_opts=opts.ItemStyleOpts(color="red", opacity=0.3)),
                  ],
            symbol="diamond", symbol_size=[8, 8],
            label_opts=opts.LabelOpts(position="top", color="yellow", font_weight="bold", font_size=18,
                                      background_color="white", padding=[3, 4])
        ),
        markline_opts=opts.MarkLineOpts(
            data=[
                opts.MarkLineItem(
                    y=hand_step, name=""
                ),
                opts.MarkLineItem(
                    y=hand_step * 2, name=""
                ),
                opts.MarkLineItem(
                    y=hand_step * 3, name=""
                ),
            ],
            symbol="none",
            label_opts=opts.LabelOpts(position="right", color="yellow"),
            linestyle_opts=opts.LineStyleOpts(width=1, color='yellow', opacity=0.6, type_="dashed")
        ),
    )
)

line3 = (
    Line()
    .add_xaxis(x_data_line)
    .add_yaxis(
        "g",
        y_data5,
        is_smooth=True,
        yaxis_index=5,
        label_opts=opts.LabelOpts(is_show=False),
        symbol="emptyCircle",
        itemstyle_opts=opts.ItemStyleOpts(color='green'),
        linestyle_opts=opts.LineStyleOpts(width=2),
        markpoint_opts=opts.MarkPointOpts(
            data=[opts.MarkPointItem(type_="max", symbol_size=[18, 18],
                                     itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                       opacity=0.3)),
                  opts.MarkPointItem(type_="min", symbol_size=[18, 18],
                                     itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                       opacity=0.3)),
                  opts.MarkPointItem(symbol_size=[18, 18],
                                     coord=[x_max, yf_x_max], value=yf_x_max,
                                     itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                       opacity=0.3)),
                  opts.MarkPointItem(symbol_size=[18, 18],
                                     coord=[x_min, yf_x_min], value=yf_x_min,
                                     itemstyle_opts=opts.ItemStyleOpts(color="yellow",
                                                                       opacity=0.3)),
                  ],
            symbol="diamond", symbol_size=[3, 3],
            label_opts=opts.LabelOpts(position="right", color="yellow", font_weight="bold", font_size=18,
                                      background_color="gray", padding=[3, 4])
        ),
        markline_opts=opts.MarkLineOpts(
            data=[
                opts.MarkLineItem(
                    name="avg",
                    y=y_avg_ratio,

                )],
            symbol="none",
            linestyle_opts=opts.LineStyleOpts(width=1, color='green', opacity=0.8, type_="dashed")
        ),
    )
)

c = bar.overlap(line1).overlap(line2).overlap(line3).overlap(line)

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

head_content = content.replace('</head>', extra_code_head + '</head>')

# 向 <script> 标签中添加额外的代码
extra_code_script = """
window.addEventListener('resize', myChart.resize);
"""

split_content = head_content.rsplit('</script>', maxsplit=1)
script_content = (extra_code_script + '</script>').join(split_content)

# 保存修改后的模板文件
with open(html_file, 'w') as file:
    file.write(script_content)

# 关闭数据库连接
db.close()
