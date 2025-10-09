package com.example.currencyExchange;

import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.money.MonetaryAmount;
import java.util.Map;

@Service
public class CurrencyExchangeService {

    private static final String API_URL = "https://api.frankfurter.app/latest";

    public Double exchange(String from, String to, Double amount) {
        try {
            if (from == null || to == null || amount == null || from.isBlank() || to.isBlank()) {
                return null;
            }

            // 1️⃣ Fetch rate from Frankfurter API
            RestTemplate restTemplate = new RestTemplate();
            String url = UriComponentsBuilder.fromHttpUrl(API_URL)
                    .queryParam("amount", amount)
                    .queryParam("from", from.toUpperCase())
                    .queryParam("to", to.toUpperCase())
                    .toUriString();

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("rates")) {
                Map<String, Double> rates = (Map<String, Double>) response.get("rates");
                Double rate = rates.get(to.toUpperCase());
                if (rate != null) {
                    // 2️⃣ Use Moneta to represent and calculate currency math
                    MonetaryAmount money = Money.of(amount, from.toUpperCase());
                    MonetaryAmount converted = Money.of(rate, to.toUpperCase());
                    return converted.getNumber().doubleValue();
                }
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
