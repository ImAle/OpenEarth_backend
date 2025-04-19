package com.alejandro.OpenEarth.service;

public interface CurrencyService {
    double getPriceInEUR(String currency, Double price);
    double getPriceInSelectedCurrency(String currency, double eurPrice);
}
