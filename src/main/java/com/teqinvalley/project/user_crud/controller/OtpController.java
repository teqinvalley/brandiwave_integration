package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.common.CommonResponse;
import com.teqinvalley.project.user_crud.service.impl.OtpService;
import com.teqinvalley.project.user_crud.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

//    @Autowired
//    private OtpKafkaProducerImpl producer;

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<CommonResponse> sendOtp(@RequestParam String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            return ResponseUtil.failed("Invalid email address.");
        }
        boolean success = otpService.sendOtp(email);
        if (success) {
            return ResponseUtil.success("OTP sent to " + email);
        } else {
            return ResponseUtil.failed("Failed to send OTP. Please try again later.");
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<CommonResponse> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (email == null || otp == null) {
            return ResponseUtil.failed("Email and OTP must be provided.");
        }

        email = email.trim().toLowerCase(); // Normalize

        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            otpService.clearOtp(email);
            return ResponseUtil.success("OTP verified successfully.");
        } else {
            return ResponseUtil.failed("Invalid or expired OTP.");
        }
    }


}

