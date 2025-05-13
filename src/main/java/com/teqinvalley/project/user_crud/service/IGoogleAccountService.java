package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.dto.request.GoogleAccountDto;

import java.util.List;
import java.util.Map;

public interface IGoogleAccountService {
    public void savePages(List<Map<String, Object>> pages, String accessToken, String userEmail);

    List<GoogleAccountDto> getAccountsByToken(String token);


}
