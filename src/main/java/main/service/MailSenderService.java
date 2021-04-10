package main.service;

import javassist.Loader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService
{
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String email, String messageTheme, String message)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(email);
        mailMessage.setSubject(messageTheme);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
