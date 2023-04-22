package com.example.currency_exchange_calculator.service;

import com.example.currency_exchange_calculator.model.Rate;
import com.example.currency_exchange_calculator.model.Table;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    public static final String CORRECT_CURRENCY_VALUE = "10";
    public static final String INCORRECT_CURRENCY_VALUE = "abc";

    @Mock
    ObjectMapper objectMapper;

    private CalculatorService calculatorService;
    private Table table;
    private double midValue;
    private boolean first;

    @BeforeEach
    public void setup() {
        calculatorService = new CalculatorService(objectMapper);
        table = Table.builder()
                .table("A")
                .currency("funt szterling")
                .code("GBP")
                .rates(Collections.singletonList(new Rate("078/A/NBP/2023", LocalDate.now(), 5.10)))
                .build();
    }

    @Test
    public void testGetMidValueSuccess() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenReturn(table);

        // when
        midValue = calculatorService.getMidValue();

        // then
        assertEquals(5.10, midValue, 0.001);
    }

    @Test
    public void testGetMidValueNullTable() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenReturn(null);

        // when
        midValue = calculatorService.getMidValue();

        // then
        assertEquals(Integer.MIN_VALUE, midValue, 0.001);
    }

    @Test
    public void testGetMidValueIOException() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenThrow(new IOException("test"));

        // when
        midValue = calculatorService.getMidValue();

        // then
        assertEquals(Integer.MIN_VALUE, midValue, 0.001);
    }

    @Test
    public void testConvertCurrencyValueForFirstCurrency() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenReturn(table);
        first = true;

        // when
        BigDecimal convertedValue = calculatorService.convertCurrencyValue(CORRECT_CURRENCY_VALUE, first);

        // then
        BigDecimal expectedValue = BigDecimal.valueOf(51).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedValue, convertedValue);
    }

    @Test
    public void testConvertCurrencyValueForSecondCurrency() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenReturn(table);
        first = false;

        // when
        BigDecimal convertedValue = calculatorService.convertCurrencyValue(CORRECT_CURRENCY_VALUE, first);

        // then
        BigDecimal expectedValue = BigDecimal.valueOf(1.96).setScale(2, RoundingMode.HALF_UP);
        assertEquals(expectedValue, convertedValue);
    }

    @Test
    public void testConvertCurrencyValueForInvalidInput() throws Exception {
        // given
        when(objectMapper.readValue(any(URL.class), ArgumentMatchers.eq(Table.class))).thenReturn(table);
        first = true;

        // when & then
        assertThrows(NumberFormatException.class, () -> calculatorService.convertCurrencyValue(INCORRECT_CURRENCY_VALUE, first));
    }
}
