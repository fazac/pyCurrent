package com.stock.pycurrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "board_code")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardCode {
    @Id
    @Column(name = "trade_date", nullable = false, length = 8)
    private String tradeDate;

    @Column(name = "code_value", length = 512)
    private String codeValue;

}