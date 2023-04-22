package com.example.currency_exchange_calculator.controller;

import com.example.currency_exchange_calculator.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTest {

    private String currencyValue1;
    private String currencyValue2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculatorService calculatorService;

    @Test
    public void testGetCalculatorPage() throws Exception {
        // given
        double midValue = 4.2;
        given(calculatorService.getMidValue()).willReturn(midValue);

        // when
        mockMvc.perform(get("/"))

        // then
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attribute("midValue", midValue));
    }

    @Test
    public void testExchangeCurrencyForRedirectToGetCalculatorPage() throws Exception {
        //given
        currencyValue1 = "100";
        currencyValue2 = "";
        BigDecimal calculatedValueCurrency2 = new BigDecimal("22.22");
        given(calculatorService.convertCurrencyValue(currencyValue1, true)).willReturn(calculatedValueCurrency2);

        //when
        mockMvc.perform(post("/exchange")
                        .param("currencyValue1", currencyValue1)
                        .param("currencyValue2", currencyValue2))

        //then
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testExchangeCurrencyShouldAddFlashAttributesForCurrencyValue1AndConvertedCurrencyValue2() throws Exception {
        //givem
        currencyValue1 = "100";
        currencyValue2 = "";
        BigDecimal calculatedValueCurrency2 = new BigDecimal("22.22");
        given(calculatorService.convertCurrencyValue(currencyValue1, true)).willReturn(calculatedValueCurrency2);

        //when
        mockMvc.perform(post("/exchange")
                        .param("currencyValue1", currencyValue1)
                        .param("currencyValue2", currencyValue2))

        //then
                .andExpect(flash().attribute("currencyValue1", currencyValue1))
                .andExpect(flash().attribute("currencyValue2", calculatedValueCurrency2));
    }

    @Test
    public void testExchangeCurrencyShouldAddFlashAttributeForNegativeCurrencyValue() throws Exception {
        //given
        currencyValue1 = "-100";
        //when
        mockMvc.perform(post("/exchange")
                        .param("currencyValue1", currencyValue1))

        //then
                .andExpect(flash().attributeExists("error_message"));
    }

    @Test
    public void testExchangeCurrencyForAddFlashAttributeForErrorMessageIfRandomSituationOccured() throws Exception {
        //given
        currencyValue1 = null;
        currencyValue2 = null;
        //when
        mockMvc.perform(post("/exchange")
                        .param("currencyValue1", currencyValue1)
                        .param("currencyValue2", currencyValue2))

        //then
                .andExpect(flash().attributeExists("error_message"));
    }

}
