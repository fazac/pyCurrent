# import akshare as ak
import aklocal as ak

# from datetime import datetime
#
# df = ak.fund_etf_spot_em()
# df = ak.stock_zh_a_spot_em()
# df = ak.stock_zh_a_hist()
# df = ak.code_id_map_em()
# df = ak.stock_board_concept_name_em()
# df = ak.stock_board_concept_cons_em()
# df = ak.stock_board_industry_name_em()
# df = ak.stock_board_industry_cons_em('BK1036')
df = ak.get_thx_cookie()
# df = ak.stock_rank_lxsz_ths()
print(df)
# symbol = '800300'
# df = 1 if symbol.startswith('6') else 0
# if not symbol.startswith('6') and not symbol.startswith('0') and not symbol.startswith('3'):
#     print("hello")

# # df = ak.fund_open_fund_daily_em()
#
# df = ak.fund_etf_hist_em(
#     symbol="513050",
#     period="daily",
#     start_date="20000101",
#     end_date="20230201",
#     adjust="hfq",
# )

# df = ak.stock_zh_a_hist(
#     symbol="300913",
#     period="daily",
#     start_date="19900101",
#     end_date="20241231",
#     adjust="hfq",
# )
# print(df)
#
#
#
# def print_log(log_str):
#     filename = "fw.txt"
#     file = open(filename, "a")
#     file.write(datetime.now().strftime('%Y-%m-%d %H:%M:%S') + ' ' + str(log_str) + '\n')
#     file.close()
#
#
# print_log(300130)
# print_log('hello')
# print_log(df)
