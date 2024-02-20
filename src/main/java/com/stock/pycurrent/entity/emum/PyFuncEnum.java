package com.stock.pycurrent.entity.emum;

public enum PyFuncEnum {

    EM_CURRENT("stock_zh_a_spot_em", "em_real_time_stock"), //实时行情
    EM_HIS_NO_DAILY("stock_zh_a_hist", "em_d_n_stock"),
    EM_HIS_AFTER_DAILY("stock_zh_a_hist", "em_d_a_stock"),
    EM_BOARD_CONCEPT_CON("stock_board_concept_cons_em", "board_concept_con"),//板块成分
    EM_BOARD_INDUSTRY_CON("stock_board_industry_cons_em", "board_industry_con"),
    AA("xx", "nn"); //最后一条无意义

    private final String funcName;
    private final String tableName;

    PyFuncEnum(String funcName, String tableName) {
        this.funcName = funcName;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "--em_func=" + this.funcName + " --em_table=" + this.tableName;
    }

}
