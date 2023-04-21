package com.example.currency_exchange_calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
