package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.ContinuousUpPK;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "continuous_up")
@IdClass(ContinuousUpPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContinuousUp {
    @Column(name = "statics_date", length = 32)
    @Id
    private String staticsDate;

    @Column(name = "ts_code", length = 10)
    @Id
    private String tsCode;

    @Column(name = "name", length = 8)
    private String name;

    @Column(name = "pri_close", precision = 18, scale = 3)
    private BigDecimal priClose;

    @Column(name = "pri_high", precision = 18, scale = 3)
    private BigDecimal priHigh;

    @Column(name = "pri_low", precision = 18, scale = 3)
    private BigDecimal priLow;

    @Column(name = "up_days")
    private Integer upDays;

    @Column(name = "up_per", precision = 18, scale = 2)
    private BigDecimal upPer;

    @Column(name = "change_hand", precision = 18, scale = 2)
    private BigDecimal changeHand;

    @Column(name = "industry", length = 32)
    private String industry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContinuousUp that = (ContinuousUp) o;
        return Objects.equals(getStaticsDate(), that.getStaticsDate()) && Objects.equals(getTsCode(), that.getTsCode()) && Objects.equals(getName(), that.getName()) && Objects.equals(getPriClose(), that.getPriClose()) && Objects.equals(getPriHigh(), that.getPriHigh()) && Objects.equals(getPriLow(), that.getPriLow()) && Objects.equals(getUpDays(), that.getUpDays()) && Objects.equals(getUpPer(), that.getUpPer()) && Objects.equals(getChangeHand(), that.getChangeHand()) && Objects.equals(getIndustry(), that.getIndustry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStaticsDate(), getTsCode(), getName(), getPriClose(), getPriHigh(), getPriLow(), getUpDays(), getUpPer(), getChangeHand(), getIndustry());
    }
}