package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.StockCalModelPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "last_hand_pri")
@IdClass(StockCalModelPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LastHandPri {
    @Column(name = "trade_date", length = 32)
    @Id
    private String tradeDate;

    @Column(name = "ts_code", length = 32)
    @Id
    private String tsCode;

    @Column(name = "last_five_pri", precision = 18, scale = 2)
    private BigDecimal lastFivePri;

    @Column(name = "last_ten_pri", precision = 18, scale = 2)
    private BigDecimal lastTenPri;

    @Column(name = "last_twenty_pri", precision = 18, scale = 2)
    private BigDecimal lastTwentyPri;

    @Column(name = "last_thirty_pri", precision = 18, scale = 2)
    private BigDecimal lastThirtyPri;

    @Column(name = "last_fifty_pri", precision = 18, scale = 2)
    private BigDecimal lastFiftyPri;

    @Column(name = "last_hundred_pri", precision = 18, scale = 2)
    private BigDecimal lastHundredPri;

    @Column(name = "type")
    @Id
    private int type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LastHandPri that = (LastHandPri) o;
        return type == that.type && Objects.equals(tradeDate, that.tradeDate) && Objects.equals(tsCode, that.tsCode) && Objects.equals(lastFivePri, that.lastFivePri) && Objects.equals(lastTenPri, that.lastTenPri) && Objects.equals(lastTwentyPri, that.lastTwentyPri) && Objects.equals(lastThirtyPri, that.lastThirtyPri) && Objects.equals(lastFiftyPri, that.lastFiftyPri) && Objects.equals(lastHundredPri, that.lastHundredPri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeDate, tsCode, lastFivePri, lastTenPri, lastTwentyPri, lastThirtyPri, lastFiftyPri, lastHundredPri, type);
    }
}