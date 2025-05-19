package com.teqinvalley.project.user_crud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "profile.file")
@Data
public class MyProfileValidation {

    private List<String> allowedTypes;
    private long maxSizeKb;
}
