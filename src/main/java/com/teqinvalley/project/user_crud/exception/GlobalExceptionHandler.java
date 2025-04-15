package com.teqinvalley.project.user_crud.exception;

import com.google.gson.Gson;
import com.teqinvalley.project.user_crud.common.FailedResponse;
import com.teqinvalley.project.user_crud.common.LoggingResponse;
import com.teqinvalley.project.user_crud.enumeration.MessageTypeConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final Gson gson;

    public GlobalExceptionHandler(Gson gson) {
        this.gson = gson;
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public FailedResponse<?> handleUserAlreadyExistsException(WebRequest wb, UserAlreadyExistException ex) {
        String msg = ex.getMessage();
        log.error(gson.toJson(LoggingResponse.builder()
                .message(msg)
                .messageTypeId(MessageTypeConst.ERROR)
                .statusCode(HttpStatus.CONFLICT)
                .path(wb.getDescription(false))
                .build()));

        return FailedResponse.fail(msg, wb.getDescription(false));
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FailedResponse<?> handleValidationExceptions(org.springframework.web.bind.MethodArgumentNotValidException ex, WebRequest wb) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String msg = "Validation failed";

        log.error(gson.toJson(LoggingResponse.builder()
                .message(msg)
                .messageTypeId(MessageTypeConst.ERROR)
                .statusCode(HttpStatus.BAD_REQUEST)
                .path(wb.getDescription(false))
                .build()));

        return FailedResponse.fail(msg, errors);
    }


}
