package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.BasicStockPK;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.util.Objects;


@Entity
@Table(name = "last_hand_pri")
@IdClass(BasicStockPK.class)
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

    @Column(name = "current_pri", precision = 18, scale = 2)
    private BigDecimal currentPri;

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


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        LastHandPri that = (LastHandPri) o;
        return getTradeDate() != null && Objects.equals(getTradeDate(), that.getTradeDate())
               && getTsCode() != null && Objects.equals(getTsCode(), that.getTsCode());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(tradeDate, tsCode);
    }
}