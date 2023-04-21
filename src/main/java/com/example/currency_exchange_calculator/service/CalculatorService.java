package com.example.currency_exchange_calculator.service;

import com.example.currency_exchange_calculator.model.Table;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.currency_exchange_calculator.util.Constants.RESOURCE_URL;

@Service
public class CalculatorService {

    private final ObjectMapper objectMapper;

    @Autowired
    public CalculatorService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.findAndRegisterModules();
    }

    public double getMidValue() {
        Table table = null;
        try {
            table = objectMapper.readValue(new URL(RESOURCE_URL), Table.class);
        } catch (MalformedURLException m) {
            m.printStackTrace();
        } catch (StreamReadException s) {
            s.printStackTrace();
        } catch (DatabindException d) {
            d.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        if (table == null)
            return Integer.MIN_VALUE;
        return table.getRates().get(0).getMid();
    }
}
