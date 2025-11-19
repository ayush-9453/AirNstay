package com.cognizant.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    // Injected from application.properties: spring.mail.username
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmailWithAttachment(
            String to,
            String subject,
            String body,
            byte[] attachment,
            String attachmentName
    ) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        // Use MimeMessageHelper for multipart messages (i.e., with attachments)
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        // Add the PDF attachment
        helper.addAttachment(attachmentName, new ByteArrayResource(attachment));

        javaMailSender.send(mimeMessage);
    }
}
