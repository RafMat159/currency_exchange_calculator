package com.example.currency_exchange_calculator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rate {
    private String no;
    private LocalDate effectiveDate;
    private double mid;
}
