package com.stock.pycurrent.entity.model;

import java.math.BigDecimal;

public class Constants {

    public static String PERIOD_DAILY = "daily";
    public static String REHABILITATION_NO = "";
    public static String REHABILITATION_AFTER = "hfq";
    public static String EM_DEFAULT_START_DAY = "19860101";
    public static int EM_PULL_HOUR = 16;
    public static String AKSHARE_EM_HIS = "akshare_his_stock_cur.py";
    public static String AKSHARE_EM_REALTIME = "akshare_stock_realtime.py";
    public static String AKSHARE_STOCK_PY = "akshare_stock_cur.py";

    public static String SSE_RT_LIST = "sse_rt_list";
    public static String SSE_RT_HIS = "sse_rt_his";
    public static String SSE_MSG_HIS = "sse_msg_his";
    public static String CLIENT_ID = "sse_client_id";
    public static BigDecimal HUNDRED = BigDecimal.valueOf(100);
    public static BigDecimal EIGHT = BigDecimal.valueOf(8);
    public static BigDecimal EIGHTEEN = BigDecimal.valueOf(18);

    public static BigDecimal PRICE_MAX = BigDecimal.valueOf(999999);
    public static BigDecimal PRICE_MIN = BigDecimal.valueOf(-1);

    public static BigDecimal FIVE = BigDecimal.valueOf(5);
    public static BigDecimal THREE = BigDecimal.valueOf(3);
    public static BigDecimal N_ONE = BigDecimal.valueOf(-1);

    public static BigDecimal N_THREE = BigDecimal.valueOf(-3);
    public static BigDecimal N_SEVEN = BigDecimal.valueOf(-7);

    public static BigDecimal FOUR_BILLION = BigDecimal.valueOf(4000000000L);

    public static BigDecimal ONE_HUNDRED_MILLION = BigDecimal.valueOf(100000000L);

    public static BigDecimal PE_LIMIT = BigDecimal.valueOf(25);

}
