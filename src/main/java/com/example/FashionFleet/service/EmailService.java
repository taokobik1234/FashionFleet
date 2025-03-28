package com.example.FashionFleet.service;

import com.example.FashionFleet.domain.dto.request.MailBody;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(mailBody.to());
        simpleMailMessage.setSubject(mailBody.subject());
        simpleMailMessage.setText(mailBody.text());

        try {
            javaMailSender.send(simpleMailMessage);
            System.out.println("Email sent successfully.");
        } catch (MailException e) {
            System.err.println("Error while sending email: " + e.getMessage());
            throw new RuntimeException("Could not send email", e); // Or handle as needed
        }
    }
}
