package com.example.system1.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.scheduling.annotation.Async
    public void sendEmail(String to, String subject, String text) {
        System.out.println("======================================");
        System.out.println("MOCK EMAIL SENT TO: " + to);
        System.out.println("SUBJECT: " + subject);
        System.out.println("BODY: " + text);
        System.out.println("======================================");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@unifind.edu");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Note: Real email sending failed (dummy server not running), but email was logged above. Error: " + e.getMessage());
        }
    }
}
