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
    public void wellcome_email(String to){
        this.sendSimpleEmail(to, "Welcome to OpenEarth!",
                "You have successfully registered! Start searching for your dream vacation home!");

    }

    @Override
    public void rented_email(String to, String houseTitle) {
        this.sendSimpleEmail(to, "Your Rented Dream Vacation home!",
                houseTitle + " has been successfully rented!");
    }

    @Override
    public void cancel_email(String to, String houseTitle) {
        this.sendSimpleEmail(to, "The cancelation for " + houseTitle + " has been done!",
                "You have successfully canceled your rent for " + houseTitle);
    }

}
