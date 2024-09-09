package com.daurenassanbaev.firstpetproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final RestTemplate restTemplate;
    public BigDecimal getExchangeRate(String currency) {
        String url = "https://api.exchangerate-api.com/v4/latest/USD";
        // response
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String body = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> currencyMap = null;
        HashMap<String, Object> exchangeRateMap = null;
        try {
            currencyMap = objectMapper.readValue(body, HashMap.class);
            exchangeRateMap = (HashMap<String, Object>) currencyMap.get("rates");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Object result = null;

        if (currency.equals("USD")) {
            result = exchangeRateMap.get("KZT");
        } else if (currency.equals("EUR")) {
            result = exchangeRateMap.get("EUR");
        } else if (currency.equals("RUB")) {
            result = exchangeRateMap.get("RUB");
        }
        BigDecimal res = BigDecimal.valueOf((Double) result);
        System.out.println(res);
        return res;
    }

}
