package com.teqinvalley.project.user_crud.common;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class LoggingResponse {
    private String message;
    private String messageTypeId;
    private HttpStatus statusCode;
    private String path;
}

