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

    @Column(name = "c_00a")
    private Integer c00a;
    @Column(name = "c_00u")
    private Integer c00u;
    @Column(name = "c_00_5u")
    private Integer c005u;
    @Column(name = "c_00_7d")
    private Integer c007d;

    @Column(name = "c_30a")
    private Integer c30a;
    @Column(name = "c_30u")
    private Integer c30u;
    @Column(name = "c_30_5u")
    private Integer c305u;
    @Column(name = "c_30_7d")
    private Integer c307d;

    @Column(name = "c_60a")
    private Integer c60a;
    @Column(name = "c_60u")
    private Integer c60u;
    @Column(name = "c_60_5u")
    private Integer c605u;
    @Column(name = "c_60_7d")
    private Integer c607d;


    public CurCount(String tradeDate, Integer... ca) {
        this.tradeDate = tradeDate;

        this.c00a = ca[0];
        this.c00u = ca[1];
        this.c005u = ca[2];
        this.c007d = ca[3];

        this.c30a = ca[4];
        this.c30u = ca[5];
        this.c305u = ca[6];
        this.c307d = ca[7];

        this.c60a = ca[8];
        this.c60u = ca[9];
        this.c605u = ca[10];
        this.c607d = ca[11];
    }

}