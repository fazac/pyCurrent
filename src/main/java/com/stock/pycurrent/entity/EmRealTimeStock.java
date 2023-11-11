package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "em_real_time_stock")
@IdClass(BasicStockPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmRealTimeStock {
    @Id
    @Column(name = "trade_date", length = 32)
    private String tradeDate;
    @Id
    @Column(name = "ts_code", length = 10)
    private String tsCode;

    @Column(name = "name", length = 8)
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

    @Column(name = "vibration", precision = 18, scale = 2)
    private BigDecimal vibration;

    @Column(name = "pri_high", precision = 18, scale = 2)
    private BigDecimal priHigh;

    @Column(name = "pri_low", precision = 18, scale = 2)
    private BigDecimal priLow;

    @Column(name = "pri_open", precision = 18, scale = 2)
    private BigDecimal priOpen;

    @Column(name = "pri_close_pre", precision = 18, scale = 2)
    private BigDecimal priClosePre;

    @Column(name = "vol_ratio", precision = 18, scale = 2)
    private BigDecimal volRatio;

    @Column(name = "change_hand", precision = 18, scale = 2)
    private BigDecimal changeHand;

    @Column(name = "pe", precision = 18, scale = 2)
    private BigDecimal pe;

    @Column(name = "pb", precision = 18, scale = 2)
    private BigDecimal pb;

    @Column(name = "market_cap", precision = 18, scale = 2)
    private BigDecimal marketCap;

    @Column(name = "circulation_market_cap", precision = 18, scale = 2)
    private BigDecimal circulationMarketCap;

    @Column(name = "increase_ratio", precision = 18, scale = 2)
    private BigDecimal increaseRatio;

    @Column(name = "five_minutes_increase_ratio", precision = 18, scale = 2)
    private BigDecimal fiveMinutesIncreaseRatio;

    @Column(name = "sixty_minutes_increase_ratio", precision = 18, scale = 2)
    private BigDecimal sixtyMinutesIncreaseRatio;

    @Column(name = "current_year_ratio", precision = 18, scale = 2)
    private BigDecimal currentYearRatio;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmRealTimeStock that = (EmRealTimeStock) o;
        return Objects.equals(tradeDate, that.tradeDate) && Objects.equals(tsCode, that.tsCode) && Objects.equals(name, that.name) && Objects.equals(currentPri, that.currentPri) && Objects.equals(pctChg, that.pctChg) && Objects.equals(amChg, that.amChg) && Objects.equals(vol, that.vol) && Objects.equals(amount, that.amount) && Objects.equals(vibration, that.vibration) && Objects.equals(priHigh, that.priHigh) && Objects.equals(priLow, that.priLow) && Objects.equals(priOpen, that.priOpen) && Objects.equals(priClosePre, that.priClosePre) && Objects.equals(volRatio, that.volRatio) && Objects.equals(changeHand, that.changeHand) && Objects.equals(pe, that.pe) && Objects.equals(pb, that.pb) && Objects.equals(marketCap, that.marketCap) && Objects.equals(circulationMarketCap, that.circulationMarketCap) && Objects.equals(increaseRatio, that.increaseRatio) && Objects.equals(fiveMinutesIncreaseRatio, that.fiveMinutesIncreaseRatio) && Objects.equals(sixtyMinutesIncreaseRatio, that.sixtyMinutesIncreaseRatio) && Objects.equals(currentYearRatio, that.currentYearRatio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tsCode, name, currentPri, pctChg, amChg, vol, amount, vibration, priHigh, priLow, priOpen, priClosePre, volRatio, changeHand, pe, pb, marketCap, circulationMarketCap, increaseRatio, fiveMinutesIncreaseRatio, sixtyMinutesIncreaseRatio, currentYearRatio);
    }
}