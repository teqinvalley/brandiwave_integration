package com.teqinvalley.project.user_crud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqinvalley.project.user_crud.dto.request.OtpMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OtpKafkaProducerImpl {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "otp-topic";

    public void sendOtp(String email, String otp) {
        OtpMessageDto msg = new OtpMessageDto();
        msg.setEmail(email);
        msg.setOtp(otp);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(msg);
            kafkaTemplate.send(TOPIC, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
