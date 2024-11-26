package in.ashokit.controller;

import org.hibernate.boot.model.source.internal.hbm.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harsh"); // Ensure this matches your configured email
            message.setTo(to); // Recipient email
            message.setSubject(subject); // Email subject
            message.setText(text); // Email content

            javaMailSender.send(message); // Send the email
            System.out.println("Email sent successfully to " + to);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
