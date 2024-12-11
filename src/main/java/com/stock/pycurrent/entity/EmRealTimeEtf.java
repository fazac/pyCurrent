package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "em_real_time_etf")
@IdClass(BasicStockPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EmRealTimeEtf {
    @Id
    @Column(name = "trade_date", length = 32)
    private String tradeDate;
    @Id
    @Column(name = "ts_code", length = 10)
    private String tsCode;

    @Column(name = "name", length = 8)
    private String name;

    @Column(name = "current_pri", precision = 18, scale = 3)
    private BigDecimal currentPri;

    @Column(name = "ipvo", precision = 18, scale = 3)
    private BigDecimal ipvo;

    @Column(name = "discount_ratio", precision = 18, scale = 3)
    private BigDecimal discountRatio;

    @Column(name = "am_chg", precision = 18, scale = 3)
    private BigDecimal amChg;

    @Column(name = "pct_chg", precision = 18, scale = 3)
    private BigDecimal pctChg;

    @Column(name = "vol")
    private Integer vol;

    @Column(name = "amount", precision = 18, scale = 3)
    private BigDecimal amount;

    @Column(name = "pri_open", precision = 18, scale = 3)
    private BigDecimal priOpen;

    @Column(name = "pri_high", precision = 18, scale = 3)
    private BigDecimal priHigh;

    @Column(name = "pri_low", precision = 18, scale = 3)
    private BigDecimal priLow;

    @Column(name = "pri_close_pre", precision = 18, scale = 3)
    private BigDecimal priClosePre;

    @Column(name = "vibration", precision = 18, scale = 3)
    private BigDecimal vibration;

    @Column(name = "change_hand", precision = 18, scale = 3)
    private BigDecimal changeHand;

    @Column(name = "vol_ratio", precision = 18, scale = 3)
    private BigDecimal volRatio;

    @Column(name = "commission_ratio", precision = 18, scale = 3)
    private BigDecimal commissionRatio;

    @Column(name = "outer_disc", precision = 18, scale = 3)
    private BigDecimal outerDisc;

    @Column(name = "inner_disc", precision = 18, scale = 3)
    private BigDecimal innerDisc;

    @Column(name = "main_fund", precision = 18, scale = 3)
    private BigDecimal mainFund;

    @Column(name = "main_fund_per", precision = 18, scale = 3)
    private BigDecimal mainFundPer;

    @Column(name = "larger_order", precision = 18, scale = 3)
    private BigDecimal largerOrder;

    @Column(name = "larger_order_per", precision = 18, scale = 3)
    private BigDecimal largerOrderPer;

    @Column(name = "large_order", precision = 18, scale = 3)
    private BigDecimal largeOrder;

    @Column(name = "large_order_per", precision = 18, scale = 3)
    private BigDecimal largeOrderPer;

    @Column(name = "medium_order", precision = 18, scale = 3)
    private BigDecimal mediumOrder;

    @Column(name = "medium_order_per", precision = 18, scale = 3)
    private BigDecimal mediumOrderPer;

    @Column(name = "small_order", precision = 18, scale = 3)
    private BigDecimal smallOrder;

    @Column(name = "small_order_per", precision = 18, scale = 3)
    private BigDecimal smallOrderPer;

    @Column(name = "cur_hand", precision = 18, scale = 3)
    private BigDecimal curHand;

    @Column(name = "buy_first", precision = 18, scale = 3)
    private BigDecimal buyFirst;

    @Column(name = "sell_first", precision = 18, scale = 3)
    private BigDecimal sellFirst;

    @Column(name = "latest_share", precision = 18, scale = 3)
    private BigDecimal latestShare;

    @Column(name = "circulation_market_cap", precision = 18, scale = 3)
    private BigDecimal circulationMarketCap;

    @Column(name = "market_cap", precision = 18, scale = 3)
    private BigDecimal marketCap;

    @Column(name = "data_date", length = 16)
    private String dataDate;

    @Column(name = "cur_date", length = 32)
    private String curDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmRealTimeEtf that = (EmRealTimeEtf) o;
        return Objects.equals(tradeDate, that.tradeDate) && Objects.equals(tsCode, that.tsCode) && Objects.equals(name, that.name) && Objects.equals(currentPri, that.currentPri) && Objects.equals(ipvo, that.ipvo) && Objects.equals(discountRatio, that.discountRatio) && Objects.equals(amChg, that.amChg) && Objects.equals(pctChg, that.pctChg) && Objects.equals(vol, that.vol) && Objects.equals(amount, that.amount) && Objects.equals(priOpen, that.priOpen) && Objects.equals(priHigh, that.priHigh) && Objects.equals(priLow, that.priLow) && Objects.equals(priClosePre, that.priClosePre) && Objects.equals(vibration, that.vibration) && Objects.equals(changeHand, that.changeHand) && Objects.equals(volRatio, that.volRatio) && Objects.equals(commissionRatio, that.commissionRatio) && Objects.equals(outerDisc, that.outerDisc) && Objects.equals(innerDisc, that.innerDisc) && Objects.equals(mainFund, that.mainFund) && Objects.equals(mainFundPer, that.mainFundPer) && Objects.equals(largerOrder, that.largerOrder) && Objects.equals(largerOrderPer, that.largerOrderPer) && Objects.equals(largeOrder, that.largeOrder) && Objects.equals(largeOrderPer, that.largeOrderPer) && Objects.equals(mediumOrder, that.mediumOrder) && Objects.equals(mediumOrderPer, that.mediumOrderPer) && Objects.equals(smallOrder, that.smallOrder) && Objects.equals(smallOrderPer, that.smallOrderPer) && Objects.equals(curHand, that.curHand) && Objects.equals(buyFirst, that.buyFirst) && Objects.equals(sellFirst, that.sellFirst) && Objects.equals(latestShare, that.latestShare) && Objects.equals(circulationMarketCap, that.circulationMarketCap) && Objects.equals(marketCap, that.marketCap) && Objects.equals(dataDate, that.dataDate) && Objects.equals(curDate, that.curDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tsCode, name, currentPri, ipvo, discountRatio, amChg, pctChg, vol, amount, priOpen, priHigh, priLow, priClosePre, vibration, changeHand, volRatio, commissionRatio, outerDisc, innerDisc, mainFund, mainFundPer, largerOrder, largerOrderPer, largeOrder, largeOrderPer, mediumOrder, mediumOrderPer, smallOrder, smallOrderPer, curHand, buyFirst, sellFirst, latestShare, circulationMarketCap, marketCap, dataDate, curDate);
    }
}