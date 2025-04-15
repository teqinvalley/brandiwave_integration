package com.teqinvalley.project.user_crud.utils;


import com.teqinvalley.project.user_crud.common.CommonResponse;
import com.teqinvalley.project.user_crud.common.FailedResponse;
import com.teqinvalley.project.user_crud.common.PaginationResponse;
import com.teqinvalley.project.user_crud.common.SuccessResponse;
import com.teqinvalley.project.user_crud.enumeration.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<CommonResponse> success(T data) {
        return new ResponseEntity<>(SuccessResponse
                .success(data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> success(ResponseStatus status, String message, T data) {
        return new ResponseEntity<>(SuccessResponse.of(status, message, data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> success(String message, T data) {
        return new ResponseEntity<>(SuccessResponse.of(ResponseStatus.SUCCESS, message, data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> success(ResponseStatus status, String message) {
        return new ResponseEntity<>(SuccessResponse.of(status, message), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> success(Long id) {
        return new ResponseEntity<>(SuccessResponse.of(id), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> success(Long id, T data) {
        return new ResponseEntity<>(SuccessResponse.of(id, data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> pagination(int pageNumber, int pageSize, T data) {
        return new ResponseEntity<>(PaginationResponse.of(pageNumber, pageSize, data), HttpStatus.OK);
    }

    //    failed response
    public static <T> ResponseEntity<CommonResponse> failed(T data) {
        return new ResponseEntity<>(FailedResponse.fail(data), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <T> ResponseEntity<CommonResponse> failed(ResponseStatus status, String message, T data) {
        return new ResponseEntity<>(FailedResponse.of(status, message, data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> failed(String message, T data) {
        return new ResponseEntity<>(FailedResponse.of(ResponseStatus.FAILURE, message, data), HttpStatus.OK);
    }

    public static <T> ResponseEntity<CommonResponse> failed(ResponseStatus status, String message) {
        return new ResponseEntity<>(FailedResponse.of(status, message), HttpStatus.OK);
    }
/*    public static <T> ResponseEntity<CommonResponse> failed(Long id) {
        return new ResponseEntity<>(FailedResponse.of(id), HttpStatus.OK);
    }
    public static <T> ResponseEntity<CommonResponse> failed(Long id, T data) {
        return new ResponseEntity<>(FailedResponse.of(id, data), HttpStatus.OK);
    }*/

}
