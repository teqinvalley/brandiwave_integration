package com.teqinvalley.project.user_crud.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teqinvalley.project.user_crud.enumeration.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.Optional;

public class FailedResponse<T> implements CommonResponse {
    private final ResponseStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private final ZonedDateTime timestamp;
    private final String message;
    private final Object errorFields;
    private final Optional<T> data;

//    private final Optional<T> id;

    public FailedResponse(ResponseStatus status, ZonedDateTime timestamp, String message, Optional<T> data, Object errorFields) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.data=data;
        this.errorFields = errorFields;
    }

    public static <T> FailedResponse<T> of(ResponseStatus status, ZonedDateTime timestamp, String message, T data) {
        return new FailedResponse<>(status, timestamp, message, Optional.ofNullable(data),null/*, Optional.empty()*/);
    }

    public static <T> FailedResponse<T> of(ResponseStatus status, String message, T data) {
        return new FailedResponse<>(status, ZonedDateTime.now(), message, Optional.ofNullable(data),null/*,Optional.empty()*/);
    }

    public static <T> FailedResponse<T> of(ResponseStatus status, String message) {
        return new FailedResponse<>(status, ZonedDateTime.now(), message, Optional.empty(),null/*,Optional.empty()*/);
    }

    public static <T> FailedResponse<T> fail(T data) {
        return new FailedResponse<>(ResponseStatus.FAILURE, ZonedDateTime.now(), "Failed", Optional.ofNullable(data),null/*,Optional.empty()*/);
    }

    public static <T> FailedResponse<T> fail(String message,Object errorFields) {
        return new FailedResponse<>(ResponseStatus.FAILURE, ZonedDateTime.now(), message, null,errorFields/*,Optional.empty()*/);
    }
/*    public static <T> FailedResponse<T> of (T id) {
        return new FailedResponse<>(ResponseStatus.FAILURE, ZonedDateTime.now(), "Failed", Optional.empty(),Optional.ofNullable(id));
    }
    public static <T> FailedResponse<T> of (T id, T data) {
        return new FailedResponse<>(ResponseStatus.FAILURE, ZonedDateTime.now(), "Failed", Optional.ofNullable(data),Optional.ofNullable(id));
    }*/

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
