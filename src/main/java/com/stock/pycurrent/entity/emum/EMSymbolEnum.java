package com.stock.pycurrent.entity.emum;

public enum EMSymbolEnum {
    EM_STOCK("stock_zh_a_spot_em", "代码"),
    EM_CONCEPT("stock_board_concept_name_em", "板块名称"),
    EM_INDUSTRY("stock_board_industry_name_em", "板块名称"),

    AA("xx", "nn"); //最后一条无意义

    private final String emSymbolFunc;
    private final String emSymbolName;

    EMSymbolEnum(String emSymbolFunc, String emSymbolName) {
        this.emSymbolFunc = emSymbolFunc;
        this.emSymbolName = emSymbolName;
    }

    @Override
    public String toString() {
        return " --em_symbol_func=" + this.emSymbolFunc + " --em_symbol_name=" + this.emSymbolName;
    }
}
