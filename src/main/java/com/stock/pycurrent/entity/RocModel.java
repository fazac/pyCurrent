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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RocModel rocModel = (RocModel) o;
        return Objects.equals(getId(), rocModel.getId()) && Objects.equals(getCreateTime(), rocModel.getCreateTime()) && Objects.equals(getCount(), rocModel.getCount()) && Objects.equals(getRatio(), rocModel.getRatio()) && Objects.equals(getCurClosePri(), rocModel.getCurClosePri()) && Objects.equals(getDoorPri(), rocModel.getDoorPri()) && Objects.equals(getStartDate(), rocModel.getStartDate()) && Objects.equals(getEndDate(), rocModel.getEndDate()) && Objects.equals(getTsCode(), rocModel.getTsCode()) && Objects.equals(getConceptSymbol(), rocModel.getConceptSymbol()) && Objects.equals(getIndustrySymbol(), rocModel.getIndustrySymbol()) && Objects.equals(getCapInfo(), rocModel.getCapInfo()) && Objects.equals(getParams(), rocModel.getParams());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreateTime(), getCount(), getRatio(), getCurClosePri(), getDoorPri(), getStartDate(), getEndDate(), getTsCode(), getConceptSymbol(), getIndustrySymbol(), getCapInfo(), getParams());
    }
}