package com.cognizant.demo.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field; 
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        Field fromEmailField = EmailService.class.getDeclaredField("fromEmail");
        fromEmailField.setAccessible(true);
        fromEmailField.set(emailService, "airnstayofficial@gmail.com"); 
    }

    @Test
    void sendEmailWithAttachment_Success() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
 
        doNothing().when(mailSender).send(any(MimeMessage.class));

        String to = "test@example.com";
        String subject = "Booking Confirmation";
        String body = "Your booking is confirmed.";
        byte[] attachment = "PDF content".getBytes(StandardCharsets.UTF_8);
        String attachmentName = "Booking.pdf";

        assertDoesNotThrow(() ->
            emailService.sendEmailWithAttachment(to, subject, body, attachment, attachmentName)
        );

  
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }


}