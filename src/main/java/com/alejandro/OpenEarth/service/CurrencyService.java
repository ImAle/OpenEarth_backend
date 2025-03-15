package com.alejandro.OpenEarth.service;

public interface CurrencyService {
    double getPriceInEUR(String currency, double price);
    double getPriceInSelectedCurrency(String currency, double eurPrice);
}
