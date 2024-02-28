/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailVerificationManager {

    private static final short MAIL_OTP_LENGTH = 6;
    private static final String OTP_SUBJECT = "One time password";

    private final String mailServer;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailVerificationManager(@Value("${mail.server}") String mailServer, JavaMailSender javaMailSender) {
        this.mailServer = mailServer;
        this.mailSender = javaMailSender;
    }

    public int generateOneTimePassword() {
        final var sb = new StringBuilder();
        for (short i = 0; i < MAIL_OTP_LENGTH; ++i) {
            // Generate random digit within 0-9
            sb.append(new Random().nextInt(9));
        }
        return Integer.parseInt(sb.toString());
    }

    public void sendEmail(String emailAddress, long otp) {

        if (StringUtils.isBlank(emailAddress)) {
            return;
        }

        final SimpleMailMessage message = createMailMessage(emailAddress, otp);
        mailSender.send(message);
    }

    private SimpleMailMessage createMailMessage(String emailAddress, long otp) {

        final var message = new SimpleMailMessage();
        message.setFrom(mailServer);
        message.setTo(emailAddress);
        message.setSubject(OTP_SUBJECT);
        message.setText(createOtpMessageText(otp));
        return message;
    }

    private static String createOtpMessageText(long otp) {
        return "Your OTP is " + otp + ". It is expiring in 1 minute";
    }
}