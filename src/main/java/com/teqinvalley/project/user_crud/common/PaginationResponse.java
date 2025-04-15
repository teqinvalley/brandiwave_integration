package com.teqinvalley.project.user_crud.common;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.teqinvalley.project.user_crud.enumeration.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.Optional;

public class PaginationResponse<T> implements CommonResponse {

    private final ResponseStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private final ZonedDateTime timestamp;

    private final String message;

    private  final int pageNumber;

    private  final int pageSize;

    private final Optional<T> data;


    public static <T> PaginationResponse<T> of(int pageNumber,int pageSize, T data) {
        return new PaginationResponse<>(ResponseStatus.SUCCESS, ZonedDateTime.now(), "Success", pageNumber,pageSize, Optional.ofNullable(data));
    }

    public PaginationResponse(ResponseStatus status, ZonedDateTime timestamp, String message, int pageNumber, int pageSize, Optional<T> data) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.data = data;
    }

    @Override
    public ResponseStatus status() {
        return status;
    }
    @Override
    public String message() {
        return message;
    }

    public int pageNumber() {
        return pageNumber;
    }
    public int pageSize() {
        return pageSize;
    }

    public Optional<T> data() {
        return data;
    }


}

