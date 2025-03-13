package com.alejandro.OpenEarth.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void wellcome_email(String to);
    void rented_email(String to, String houseTitle);
    void cancel_email(String to, String houseTitle);
}
