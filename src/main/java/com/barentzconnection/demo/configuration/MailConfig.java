package com.barentzconnection.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    private static final String email = "noreply.barentsconnection@gmail.com";
    private static final String pass = "Kirkenes123";
    @Bean
    public JavaMailSenderImpl getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(email);
        mailSender.setPassword(pass);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("spring.mail.properties.mail.smtp.ssl.enable","true");
        props.put("mail.default-encoding","UTF-8");
        return mailSender;
    }
}
