package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.service.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("currencyService")
public class CurrencyServiceImpl implements CurrencyService {

    private final RestTemplate restTemplate;

    public CurrencyServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public double getPriceInEUR(String currency, double price) {
        String url = "https://api.exchangerate-api.com/v4/latest/" + currency;
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        if (rates != null && rates.containsKey(currency)) {
            double rate = rates.get(currency);
            return price * rate;
        }

        throw new IllegalArgumentException("Currency not supported");
    }

    @Override
    public double getPriceInSelectedCurrency(String currency, double eurPrice) {
        String url = "https://api.exchangerate-api.com/v4/latest/EUR";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");

        if(rates != null && rates.containsKey(currency)) {
            double rate = rates.get(currency);
            return eurPrice * rate;
        }

        throw new IllegalArgumentException("Currency not supported");
    }

}
