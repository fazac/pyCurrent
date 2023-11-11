package com.stock.pycurrent.entity.emum;

public enum PyFuncEnum {

    EM_CURRENT("stock_zh_a_spot_em", "em_real_time_stock"), //实时行情

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
