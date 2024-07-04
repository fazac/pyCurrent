package com.stock.pycurrent.entity;

import com.stock.pycurrent.entity.pk.CodeLabelPK;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Entity
@IdClass(CodeLabelPK.class)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "code_label")
public class CodeLabel {
    @Column(name = "trade_date", length = 32)
    @Id
    private String tradeDate;

    @Column(name = "ts_code", length = 10)
    @Id
    private String tsCode;

    @Column(name = "industry", length = 16)
    private String industry;

    @Column(name = "concept", length = 512)
    private String concept;

    @Column(name = "name", length = 16)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CodeLabel codeLabel = (CodeLabel) o;
        return Objects.equals(getTradeDate(), codeLabel.getTradeDate()) && Objects.equals(getTsCode(), codeLabel.getTsCode()) && Objects.equals(getIndustry(), codeLabel.getIndustry()) && Objects.equals(getConcept(), codeLabel.getConcept()) && Objects.equals(getName(), codeLabel.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTradeDate(), getTsCode(), getIndustry(), getConcept(), getName());
    }
}