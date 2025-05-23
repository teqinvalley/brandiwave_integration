package com.teqinvalley.project.user_crud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqinvalley.project.user_crud.dto.request.OtpMessageDto;
import lombok.extern.slf4j.XSlf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@XSlf4j
public class OtpKafkaProducerImpl {
    private static final Logger logger = LoggerFactory.getLogger(OtpKafkaProducerImpl.class);
    private static final String TOPIC = "otp-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper(); // reuse instance

    public void sendOtp(String email, String otp) {
        OtpMessageDto msg = new OtpMessageDto();
        msg.setEmail(email);
        msg.setOtp(otp);

        try {
            String json = objectMapper.writeValueAsString(msg);
            kafkaTemplate.send(TOPIC, json).addCallback(
                    result -> logger.info("OTP message sent successfully to topic {}: {}", TOPIC, json),
                    ex -> logger.error("Failed to send OTP message to Kafka", ex)
            );
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize OTP message", e);
        }
    }
}
