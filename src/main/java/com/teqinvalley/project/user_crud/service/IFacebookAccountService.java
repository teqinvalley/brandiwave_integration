package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.dto.request.UserEngagementRequestDto;

import java.util.List;
import java.util.Map;

public interface IFacebookAccountService {
    List<FacebookAccountDto> getAccountsByToken(String token);

    public void savePages(List<Map<String, Object>> pages, String accessToken, String userEmail);


    Map<String, Object> getFacebookEngagement(UserEngagementRequestDto youtubeEngagementRequestDto) throws Exception;
}
