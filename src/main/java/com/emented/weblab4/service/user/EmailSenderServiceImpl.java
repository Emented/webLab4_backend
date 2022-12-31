package com.emented.weblab4.service.user;

import com.emented.weblab4.DAO.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    private static final String API_PATTERN = "/api/v1/auth/verify?verification_code=";
    private static final String SUBJECT = "Please verify your registration";

    private final JavaMailSender javaMailSender;

    @Value("${mail.sender.sender.email}")
    private String senderEmail;

    @Value("${mail.sender.sender.com}")
    private String senderCompanyName;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendVerificationEmail(User user, String url) throws MessagingException, UnsupportedEncodingException {

        String content = url + API_PATTERN + user.getVerificationCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(senderEmail, senderCompanyName);
        helper.setTo(user.getEmail());
        helper.setSubject(SUBJECT);

        helper.setText(content);

        javaMailSender.send(message);

        log.info("Confirmation message sent to {}", user.getEmail());

    }
}
