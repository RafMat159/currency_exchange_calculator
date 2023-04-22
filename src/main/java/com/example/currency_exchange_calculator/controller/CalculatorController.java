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
    public String exchangeCurrency(@RequestParam(name = "currency1", required = false) String currency1,
                                   @RequestParam(name = "currency2", required = false) String currency2,
                                   RedirectAttributes redirectAttributes) {
        currency1 = currency1 == null ? "" : currency1;
        currency2 = currency2 == null ? "" : currency2;
        try {
            if ("".equals(currency1) && "".equals(currency2))
                redirectAttributes.addFlashAttribute("error_message", "Fill any of the inputs");
            else if (currency1.contains("-") || currency2.contains("-"))
                redirectAttributes.addFlashAttribute("error_message", "Give a positive value");
            else if (!"".equals(currency1)) {
                currency1 = currency1.replaceFirst("^0+(?!$)", "");
                BigDecimal secondCurrencyValue = calculatorService.convertCurrency(currency1, true);
                redirectAttributes.addFlashAttribute("currency1", currency1);
                redirectAttributes.addFlashAttribute("currency2", secondCurrencyValue);
            } else if (!"".equals(currency2)) {
                currency2 = currency2.replaceFirst("^0+(?!$)", "");
                BigDecimal firstCurrencyValue = calculatorService.convertCurrency(currency2, false);
                redirectAttributes.addFlashAttribute("currency1", firstCurrencyValue);
                redirectAttributes.addFlashAttribute("currency2", currency2);
            }
        } catch (NumberFormatException n) {
            redirectAttributes.addFlashAttribute("error_message", "Wrong data was given");
        }
        return "redirect:/";
    }
}
