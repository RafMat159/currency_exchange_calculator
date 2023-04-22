package com.example.currency_exchange_calculator.controller;

import com.example.currency_exchange_calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class CalculatorController {

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/")
    public String getCalculatorPage(Model model) {

        double mid = calculatorService.getMidValue();
        if (mid == Integer.MIN_VALUE)
            model.addAttribute("error_message",
                    "Something went wrong while downloading data from the NBP website");
        else
            model.addAttribute("midValue", mid);
        return "calculator";
    }

    @PostMapping("/exchange")
    public String exchangeCurrency(@RequestParam(name = "currencyValue1", required = false) String currencyValue1,
                                   @RequestParam(name = "currencyValue2", required = false) String currencyValue2,
                                   RedirectAttributes redirectAttributes) {
        currencyValue1 = currencyValue1 == null ? "" : currencyValue1;
        currencyValue2 = currencyValue2 == null ? "" : currencyValue2;
        try {
            if ("".equals(currencyValue1) && "".equals(currencyValue2))
                redirectAttributes.addFlashAttribute("error_message", "Fill any of the inputs");
            else if (currencyValue1.contains("-") || currencyValue2.contains("-"))
                redirectAttributes.addFlashAttribute("error_message", "Give a positive value");
            else if (!"".equals(currencyValue1)) {
                currencyValue1 = currencyValue1.replaceFirst("^0+(?!$)", "");
                BigDecimal secondCurrencyValue = calculatorService.convertCurrencyValue(currencyValue1, true);
                redirectAttributes.addFlashAttribute("currencyValue1", currencyValue1);
                redirectAttributes.addFlashAttribute("currencyValue2", secondCurrencyValue);
            } else if (!"".equals(currencyValue2)) {
                currencyValue2 = currencyValue2.replaceFirst("^0+(?!$)", "");
                BigDecimal firstCurrencyValue = calculatorService.convertCurrencyValue(currencyValue2, false);
                redirectAttributes.addFlashAttribute("currencyValue1", firstCurrencyValue);
                redirectAttributes.addFlashAttribute("currencyValue2", currencyValue2);
            }
        } catch (NumberFormatException n) {
            redirectAttributes.addFlashAttribute("error_message", "Wrong data was given");
        }
        return "redirect:/";
    }
}
