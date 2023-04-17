package com.filmdoms.community.account.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncEmailSendService {

    private final JavaMailSender emailSender;

    @Async("mailAsyncExecutor")
    public void sendEmail(String email, String subject, String content, boolean html, boolean multipart) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, multipart);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, html);

            emailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("Exception thrown during email submission, message={}", e.getMessage());
        }
    }
}
