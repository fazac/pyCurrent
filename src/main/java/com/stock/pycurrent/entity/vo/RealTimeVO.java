package com.stock.pycurrent.entity.vo;

import java.math.BigDecimal;

/**
 * @author fzc
 * @date 2024/6/17 16:56
 * @description
 */

public class RealTimeVO {
    private int di; //down_index
    private BigDecimal cp;//current_price
    private Long vs;//vol_step
    private BigDecimal h;//change_hand
    private BigDecimal rt;//pch_change
    private BigDecimal bar;

    public int getDi() {
        return di;
    }

    public void setDi(int di) {
        this.di = di;
    }

    public BigDecimal getCp() {
        return cp;
    }

    public void setCp(BigDecimal cp) {
        this.cp = cp;
    }

    public Long getVs() {
        return vs;
    }

    public void setVs(Long vs) {
        this.vs = vs;
    }

    public BigDecimal getH() {
        return h;
    }

    public void setH(BigDecimal h) {
        this.h = h;
    }

    public BigDecimal getRt() {
        return rt;
    }

    public void setRt(BigDecimal rt) {
        this.rt = rt;
    }

    public BigDecimal getBar() {
        return bar;
    }

    public void setBar(BigDecimal bar) {
        this.bar = bar;
    }
}
