package com.example.currency_exchange_calculator.controller;

import com.example.currency_exchange_calculator.service.CalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
