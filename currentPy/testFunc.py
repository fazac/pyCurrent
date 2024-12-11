import akshare as ak
#
# # df = ak.fund_etf_spot_em()
# # df = ak.fund_open_fund_daily_em()
#
# df = ak.fund_etf_hist_em(
#     symbol="513050",
#     period="daily",
#     start_date="20000101",
#     end_date="20230201",
#     adjust="hfq",
# )

df = ak.stock_zh_a_hist(
    symbol="300973",
    period="daily",
    start_date="20170301",
    end_date="20241212",
    adjust="hfq",
)
print(df)

