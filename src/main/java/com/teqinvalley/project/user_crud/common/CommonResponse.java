package com.teqinvalley.project.user_crud.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.teqinvalley.project.user_crud.enumeration.ResponseStatus;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public interface CommonResponse {
    ResponseStatus status();
    String message();
}
