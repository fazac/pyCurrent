package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "em_d_a_etf")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class EmDAEtf extends BasicStock {
    @Column(name = "amplitude", precision = 18, scale = 2)
    private BigDecimal amplitude;

    @Column(name = "change_hand", precision = 18, scale = 2)
    private BigDecimal changeHand;

}