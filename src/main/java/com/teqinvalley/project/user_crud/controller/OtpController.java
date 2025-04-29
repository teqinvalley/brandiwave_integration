package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.common.CommonResponse;
import com.teqinvalley.project.user_crud.service.impl.OtpKafkaProducerImpl;
import com.teqinvalley.project.user_crud.service.impl.OtpService;
import com.teqinvalley.project.user_crud.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    @Autowired
    private OtpKafkaProducerImpl producer;

    @Autowired
    private OtpService otpService;

    @PostMapping("/send")
    public ResponseEntity<CommonResponse> sendOtp(@RequestParam String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        producer.sendOtp(email, otp);
        return ResponseUtil.success("OTP sent via Kafka.");

    }

    @PostMapping("/verify")
    public ResponseEntity<CommonResponse> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (otpService.verifyOtp(email, otp)) {
            otpService.clearOtp(email);
            return ResponseUtil.success("OTP verified.");
        } else {
            return ResponseUtil.failed("Invalid OTP.");
        }
    }
}

