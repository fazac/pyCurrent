package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

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

    @Column(name = "c_00_35u")
    private Integer c0035u;
    @Column(name = "c_00_13u")
    private Integer c0013u;
    @Column(name = "c_00_01u")
    private Integer c0001u;
    @Column(name = "c_00_01d")
    private Integer c0001d;
    @Column(name = "c_00_13d")
    private Integer c0013d;
    @Column(name = "c_00_37d")
    private Integer c0037d;


    @Column(name = "c_30a")
    private Integer c30a;
    @Column(name = "c_30u")
    private Integer c30u;
    @Column(name = "c_30_5u")
    private Integer c305u;
    @Column(name = "c_30_7d")
    private Integer c307d;

    @Column(name = "c_30_35u")
    private Integer c3035u;
    @Column(name = "c_30_13u")
    private Integer c3013u;
    @Column(name = "c_30_01u")
    private Integer c3001u;
    @Column(name = "c_30_01d")
    private Integer c3001d;
    @Column(name = "c_30_13d")
    private Integer c3013d;
    @Column(name = "c_30_37d")
    private Integer c3037d;

    @Column(name = "c_60a")
    private Integer c60a;
    @Column(name = "c_60u")
    private Integer c60u;
    @Column(name = "c_60_5u")
    private Integer c605u;
    @Column(name = "c_60_7d")
    private Integer c607d;

    @Column(name = "c_60_35u")
    private Integer c6035u;
    @Column(name = "c_60_13u")
    private Integer c6013u;
    @Column(name = "c_60_01u")
    private Integer c6001u;
    @Column(name = "c_60_01d")
    private Integer c6001d;
    @Column(name = "c_60_13d")
    private Integer c6013d;
    @Column(name = "c_60_37d")
    private Integer c6037d;
    @Column(name = "is_summary")
    private boolean isSummary;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Column(name = "zero_amount")
    private BigDecimal zeroAmount;
    @Column(name = "three_amount")
    private BigDecimal threeAmount;
    @Column(name = "six_amount")
    private BigDecimal sixAmount;


    public CurCount(String tradeDate, Integer... ca) {
        this.tradeDate = tradeDate;

        this.c00a = ca[0];
        this.c00u = ca[1];
        this.c005u = ca[2];
        this.c007d = ca[3];
        this.c0035u = ca[4];
        this.c0013u = ca[5];
        this.c0001u = ca[6];
        this.c0001d = ca[7];
        this.c0013d = ca[8];
        this.c0037d = ca[9];

        this.c30a = ca[10];
        this.c30u = ca[11];
        this.c305u = ca[12];
        this.c307d = ca[13];
        this.c3035u = ca[14];
        this.c3013u = ca[15];
        this.c3001u = ca[16];
        this.c3001d = ca[17];
        this.c3013d = ca[18];
        this.c3037d = ca[19];

        this.c60a = ca[20];
        this.c60u = ca[21];
        this.c605u = ca[22];
        this.c607d = ca[23];
        this.c6035u = ca[24];
        this.c6013u = ca[25];
        this.c6001u = ca[26];
        this.c6001d = ca[27];
        this.c6013d = ca[28];
        this.c6037d = ca[29];

    }

}