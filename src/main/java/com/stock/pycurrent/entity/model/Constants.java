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
    public static BigDecimal TEN = BigDecimal.valueOf(10);
    public static BigDecimal TWENTY = BigDecimal.valueOf(20);
    public static BigDecimal THIRTY = BigDecimal.valueOf(30);
    public static BigDecimal FIFTY = BigDecimal.valueOf(50);
    public static BigDecimal THREE = BigDecimal.valueOf(3);
    public static BigDecimal N_ONE = BigDecimal.valueOf(-1);

    public static BigDecimal N_THREE = BigDecimal.valueOf(-3);
    public static BigDecimal N_SEVEN = BigDecimal.valueOf(-7);

    public static BigDecimal FOUR_BILLION = BigDecimal.valueOf(4000000000L);

    public static BigDecimal ONE_HUNDRED_MILLION = BigDecimal.valueOf(100000000L);

    public static BigDecimal PE_LIMIT = BigDecimal.valueOf(25);

    public static int MIN_SALT_LENGTH = 10;
    public static int MAX_SALT_LENGTH = 13;
    public static String TOKEN_KEY_NAME = "token";
    public static int CACHE_EXPIRE_DAY = 3;
    public static int PAGE_SIZE = 10;
    public static int FONT_PAGE_SIZE = 5;
    public static int KEY_IMG_SIZE = 500;
    public static String JWT_ISSUER = "fazac.top";
    public static String LINE_SEPERATOR = "|";
    public static String TWO_LINE_SEPERATOR = "||";
    public static String ADMIN_PATH = "/admin/";
    public static String KEY_DIR = "totp";
    public static String KEY_NAME = ".png";
    public static String UPLOAD_URL = "/static/upload_file/";
    public static String KEY_URL = "/static/totp/";

}
