package com.alejandro.OpenEarth.service;

public interface EmailService {
    void sendSimpleEmail(String to, String subject, String text);
    void wellcomeEmail(String to);
    void rentedEmail(String to, String houseTitle);
    void cancelEmail(String to, String houseTitle);
    void sendResetPasswordEmail(String to, String token);
    void sendSuccessfullPasswordReset(String to);
}
