package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cur_count")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurCount {
    @Id
    @Column(name = "trade_date", nullable = false, length = 32)
    private String tradeDate;

    @Column(name = "c_30u")
    private Integer c30u;

    @Column(name = "c_30a")
    private Integer c30a;

    @Column(name = "c_60u")
    private Integer c60u;

    @Column(name = "c_60a")
    private Integer c60a;

    @Column(name = "c_00u")
    private Integer c00u;

    @Column(name = "c_00a")
    private Integer c00a;

}