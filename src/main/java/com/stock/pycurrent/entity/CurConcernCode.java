package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.CurConcernCodePK;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "cur_concern_code")
@IdClass(CurConcernCodePK.class)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurConcernCode {
    @Column(name = "trade_date", length = 32)
    @Id
    private String tradeDate;

    @Column(name = "ts_code", length = 10)
    @Id
    private String tsCode;

    @Column(name = "mark", length = 2)
    private String mark;

    @Column(name = "rt", precision = 7, scale = 2)
    private BigDecimal rt;

    @Column(name = "h", precision = 7, scale = 2)
    private BigDecimal h;

    @Column(name = "rr", precision = 7, scale = 2)
    private BigDecimal rr;

    @Column(name = "bp", precision = 7, scale = 2)
    private BigDecimal bp;

    @Column(name = "cp", precision = 7, scale = 2)
    private BigDecimal cp;

    @Column(name = "bar", precision = 7, scale = 2)
    private BigDecimal bar;

    @Column(name = "cm", precision = 8, scale = 3)
    private BigDecimal cm;

    @Column(name = "pe", precision = 7, scale = 2)
    private BigDecimal pe;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CurConcernCode that = (CurConcernCode) o;
        return getTradeDate() != null && Objects.equals(getTradeDate(), that.getTradeDate())
               && getTsCode() != null && Objects.equals(getTsCode(), that.getTsCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(tradeDate, tsCode);
    }
}