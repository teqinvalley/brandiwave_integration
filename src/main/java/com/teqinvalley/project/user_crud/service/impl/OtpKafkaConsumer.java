package com.teqinvalley.project.user_crud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqinvalley.project.user_crud.dto.request.OtpMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpKafkaConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpService otpService;

    @KafkaListener(topics = "otp-topic", groupId = "otp-group")
    public void consume(String message) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            OtpMessageDto otpMsg = mapper.readValue(message, OtpMessageDto.class);

            // Save OTP for verification
            otpService.saveOtp(otpMsg.getEmail(), otpMsg.getOtp());

            // Send email
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(otpMsg.getEmail());
            mail.setSubject("Your OTP Code");
            mail.setText("Your OTP is: " + otpMsg.getOtp());
            mailSender.send(mail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

