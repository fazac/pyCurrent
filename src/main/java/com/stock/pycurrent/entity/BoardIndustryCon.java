package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "board_industry_con")
@IdClass(BasicStockPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardIndustryCon {
    @Column(name = "trade_date", length = 32)
    @Id
    private String tradeDate;

    @Column(name = "symbol", length = 32)
    private String symbol;

    @Column(name = "ts_code", length = 32)
    @Id
    private String tsCode;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "current_pri", precision = 18, scale = 2)
    private BigDecimal currentPri;

    @Column(name = "pct_chg", precision = 18, scale = 2)
    private BigDecimal pctChg;

    @Column(name = "am_chg", precision = 18, scale = 2)
    private BigDecimal amChg;

    @Column(name = "vol")
    private Long vol;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "amplitude", precision = 18, scale = 2)
    private BigDecimal amplitude;

    @Column(name = "pri_high", precision = 18, scale = 2)
    private BigDecimal priHigh;

    @Column(name = "pri_low", precision = 18, scale = 2)
    private BigDecimal priLow;

    @Column(name = "pri_open", precision = 18, scale = 2)
    private BigDecimal priOpen;

    @Column(name = "pri_close_pre", precision = 18, scale = 2)
    private BigDecimal priClosePre;

    @Column(name = "change_hand", precision = 18, scale = 2)
    private BigDecimal changeHand;

    @Column(name = "pe", precision = 18, scale = 2)
    private BigDecimal pe;

    @Column(name = "pb", precision = 18, scale = 2)
    private BigDecimal pb;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardIndustryCon that = (BoardIndustryCon) o;
        return Objects.equals(getTradeDate(), that.getTradeDate()) && Objects.equals(getSymbol(), that.getSymbol()) && Objects.equals(getTsCode(), that.getTsCode()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCurrentPri(), that.getCurrentPri()) && Objects.equals(getPctChg(), that.getPctChg()) && Objects.equals(getAmChg(), that.getAmChg()) && Objects.equals(getVol(), that.getVol()) && Objects.equals(getAmount(), that.getAmount()) && Objects.equals(getAmplitude(), that.getAmplitude()) && Objects.equals(getPriHigh(), that.getPriHigh()) && Objects.equals(getPriLow(), that.getPriLow()) && Objects.equals(getPriOpen(), that.getPriOpen()) && Objects.equals(getPriClosePre(), that.getPriClosePre()) && Objects.equals(getChangeHand(), that.getChangeHand()) && Objects.equals(getPe(), that.getPe()) && Objects.equals(getPb(), that.getPb());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate(), getSymbol(), getTsCode(), getName(), getCurrentPri(), getPctChg(), getAmChg(), getVol(), getAmount(), getAmplitude(), getPriHigh(), getPriLow(), getPriOpen(), getPriClosePre(), getChangeHand(), getPe(), getPb());
    }
}