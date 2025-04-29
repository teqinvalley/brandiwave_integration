package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.TwitterAccountDto;

import java.util.List;
import java.util.Map;

public interface ITwitterAccountService {

    List<TwitterAccountDto> getAccountsByToken(String token);

    void saveAccount(Map<String, Object> twitterUserData, String accessToken, String email);
}
