/**
 * Copyright (c) 2024 Areg Abgaryan
 */

package com.areg.project.managers;

import com.areg.project.controllers.EndpointsConstants;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class EmailVerificationManager {

    private static final String OTP_SUBJECT = "One time password";

    private final short otpLength;
    private final String mailServer;

    private final JavaMailSender mailSender;

    @Autowired
    public EmailVerificationManager(@Value("${otp.length}") short otpLength, @Value("${mail.server}") String mailServer,
                                    JavaMailSender javaMailSender) {
        this.otpLength = otpLength;
        this.mailServer = mailServer;
        this.mailSender = javaMailSender;
    }

    public String generateOneTimePassword() {
        final var sb = new StringBuilder();
        for (short i = 0; i < otpLength; ++i) {
            //  Generate random digit within 0-9
            sb.append(new Random().nextInt(9));
        }
        return sb.toString();
    }

    public void sendEmail(String emailAddress, String otp) {
        if (StringUtils.isBlank(emailAddress)) {
            return;
        }
        mailSender.send(createMailMessage(emailAddress, otp));
    }

    //  Check whether the email address is valid
    public void isValidEmailAddress(String email) throws AddressException {
        final var internetAddress = new InternetAddress(email);
        internetAddress.validate();
    }

    //  Create a message for the user with OTP instructions for verifying email during sign up
    public static String createOTPInstructionsMessage(String email) {
        return "A one time password is sent to your " + email
                + " address. Please, send it via this path " + EndpointsConstants.AUTH_SIGNUP_VERIFY_EMAIL;
    }


    private SimpleMailMessage createMailMessage(String emailAddress, String otp) {
        final var message = new SimpleMailMessage();
        message.setFrom(mailServer);
        message.setTo(emailAddress);
        message.setSubject(OTP_SUBJECT);
        message.setText(createOtpMessageText(otp));
        return message;
    }

    private static String createOtpMessageText(String otp) {
        return "Your OTP is " + otp + ". It is expiring in 2 minutes";
    }
}
