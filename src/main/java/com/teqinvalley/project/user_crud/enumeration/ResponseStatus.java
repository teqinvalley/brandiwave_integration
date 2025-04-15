package com.teqinvalley.project.user_crud.enumeration;

public enum ResponseStatus {

    SUCCESS("Success"),
    FAILURE("Failure"),
    TOKEN_EXPIRED("TOKEN_EXPIRED");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public String getStatus() {
        return status;
    }
}
