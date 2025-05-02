package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    @Override
    public void wellcomeEmail(String to){
        this.sendSimpleEmail(to, "Welcome to OpenEarth!",
                "You have successfully registered! Start searching for your dream vacation home!");

    }

    @Override
    public void rentedEmail(String to, String houseTitle) {
        this.sendSimpleEmail(to, "Your Rented Dream Vacation home!",
                houseTitle + " has been successfully rented!");
    }

    @Override
    public void cancelEmail(String to, String houseTitle) {
        this.sendSimpleEmail(to, "The cancelation for " + houseTitle + " has been done!",
                "You have successfully canceled your rent for " + houseTitle + ".\nWe will put in contact with you to return you the money in less than 5 working days.");
    }

    @Override
    public void sendResetPasswordEmail(String to, String token){
        String resetUrl = "http://localhost:4200/forgot-password?token=" + token;

        this.sendSimpleEmail(to, "Password Reset Request",
                "Click the following link to reset your password: \n" + resetUrl  +
                        "\nIf you did not request a password reset, please ignore this email.");
    }

    @Override
    public void sendSuccessfullPasswordReset(String to){
        this.sendSimpleEmail(to, "Password successfully changed", "You have successfully changed your password!");
    }

}
