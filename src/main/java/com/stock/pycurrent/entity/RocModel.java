package com.stock.pycurrent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "roc_model")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RocModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sn", nullable = false)
    private Integer id;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "count")
    private Integer count;

    @Column(name = "s_count")
    private Integer sCount;

    @Column(name = "ratio", precision = 18, scale = 2)
    private BigDecimal ratio;

    @Column(name = "cur_close_pri", precision = 18, scale = 2)
    private BigDecimal curClosePri;

    @Column(name = "door_pri", precision = 18, scale = 2)
    private BigDecimal doorPri;

    @Column(name = "start_date", length = 8)
    private String startDate;

    @Column(name = "end_date", length = 8)
    private String endDate;

    @Column(name = "ts_code", length = 6)
    private String tsCode;

    @Column(name = "concept_symbol", length = 32)
    private String conceptSymbol;

    @Column(name = "industry_symbol", length = 256)
    private String industrySymbol;

    @Column(name = "cap_info", length = 32)
    private String capInfo;

    @Column(name = "params", length = 256)
    private String params;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RocModel rocModel = (RocModel) o;
        return Objects.equals(id, rocModel.id) && Objects.equals(createTime, rocModel.createTime) && Objects.equals(count, rocModel.count) && Objects.equals(sCount, rocModel.sCount) && Objects.equals(ratio, rocModel.ratio) && Objects.equals(curClosePri, rocModel.curClosePri) && Objects.equals(doorPri, rocModel.doorPri) && Objects.equals(startDate, rocModel.startDate) && Objects.equals(endDate, rocModel.endDate) && Objects.equals(tsCode, rocModel.tsCode) && Objects.equals(conceptSymbol, rocModel.conceptSymbol) && Objects.equals(industrySymbol, rocModel.industrySymbol) && Objects.equals(capInfo, rocModel.capInfo) && Objects.equals(params, rocModel.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime, count, sCount, ratio, curClosePri, doorPri, startDate, endDate, tsCode, conceptSymbol, industrySymbol, capInfo, params);
    }
}