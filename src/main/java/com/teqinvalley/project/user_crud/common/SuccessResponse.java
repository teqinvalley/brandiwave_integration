package com.teqinvalley.project.user_crud.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teqinvalley.project.user_crud.enumeration.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.Optional;


public class SuccessResponse<T> implements CommonResponse {

    private final ResponseStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private final ZonedDateTime timestamp;
    private final String message;
    private final Optional<T> data;

    private final Optional<T> id;

    public SuccessResponse(ResponseStatus status, ZonedDateTime timestamp, String message, Optional<T> data, Optional<T> id) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.data = data;
        this.id = id;
    }

    public static <T> SuccessResponse<T> of(ResponseStatus status, ZonedDateTime timestamp, String message, T data) {
        return new SuccessResponse<>(status, timestamp, message, Optional.ofNullable(data), Optional.empty());
    }

    public static <T> SuccessResponse<T> of(ResponseStatus status, String message, T data) {
        return new SuccessResponse<>(status, ZonedDateTime.now(), message, Optional.ofNullable(data),Optional.empty());
    }

    public static <T> SuccessResponse<T> of(ResponseStatus status, String message) {
        return new SuccessResponse<>(status, ZonedDateTime.now(), message, Optional.empty(),Optional.empty());
    }

    public static <T> SuccessResponse<T> success(T data) {
        return new SuccessResponse<>(ResponseStatus.SUCCESS, ZonedDateTime.now(), "Success", Optional.ofNullable(data),Optional.empty());
    }
    public static <T> SuccessResponse<T> of (T id) {
        return new SuccessResponse<>(ResponseStatus.SUCCESS, ZonedDateTime.now(), "Success", Optional.empty(),Optional.ofNullable(id));
    }
    public static <T> SuccessResponse<T> of (T id, T data) {
        return new SuccessResponse<>(ResponseStatus.SUCCESS, ZonedDateTime.now(), "Success", Optional.ofNullable(data),Optional.ofNullable(id));
    }

    @Override
    public ResponseStatus status() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }

    public ZonedDateTime timestamp() {
        return timestamp;
    }

    public Optional<T> data() {
        return data;
    }
}
