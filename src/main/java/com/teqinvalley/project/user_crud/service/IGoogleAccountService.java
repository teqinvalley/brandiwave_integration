package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.GoogleAccountDto;
import com.teqinvalley.project.user_crud.dto.request.UserEngagementRequestDto;
import com.teqinvalley.project.user_crud.dto.request.YoutubeVideoUploadDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IGoogleAccountService {
    public void savePages(List<Map<String, Object>> pages, String accessToken, String userEmail);

    List<GoogleAccountDto> getAccountsByToken(String token);

    ResponseEntity<String> uploadVideo(String accessToken, YoutubeVideoUploadDto videoUploadDto);

    void redirectToGoogleOAuthPage(String token, HttpServletResponse response) throws IOException;

    void handleGoogleCallback(String code, String stateToken, HttpServletResponse response) throws IOException;

    Map<String, Object> getYoutubeEngagement(UserEngagementRequestDto youtubeEngagementRequestDto);
}
