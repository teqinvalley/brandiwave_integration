package com.teqinvalley.project.user_crud.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private final Map<String, String> otpMap = new ConcurrentHashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendOtp(String email) {
        try {
            email = email.trim().toLowerCase(); // Normalize

            SecureRandom random = new SecureRandom();
            String otp = String.valueOf(random.nextInt(900000) + 100000);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP is: " + otp);
            mailSender.send(message); // May throw MailException

            email = email.trim().toLowerCase(); // Normalize
            otpMap.put(email, otp); // Store OTP
            System.out.println("OTP stored: " + otp + " for " + email);
            return true;

        } catch (Exception e) {
            System.err.println("Failed to send OTP to " + email + ": " + e.getMessage());
            return false;
        }
    }



    public boolean verifyOtp(String email, String otp) {
        if (email == null || otp == null) return false;

        email = email.trim().toLowerCase(); // Normalize
        String storedOtp = otpMap.get(email);
        System.out.println("Verifying OTP: input=" + otp + ", stored=" + storedOtp + ", email=" + email); // Debug

        return otp.equals(storedOtp);
    }


    public void clearOtp(String email) {
        otpMap.remove(email);
    }

}