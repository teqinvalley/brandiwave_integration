package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.GoogleAccountDto;

import java.util.List;
import java.util.Map;

public interface GoogleAccountService {

    /**
     * Save Google account data (pages/info) fetched from Google API.
     *
     * @param pages        List of Google account data maps
     * @param accessToken  The access token associated with the account
     * @param userEmail    The email extracted from the JWT
     */
    void savePages(List<Map<String, Object>> pages, String accessToken, String userEmail);

    /**
     * Get list of Google accounts associated with a user token.
     *
     * @param token JWT token containing the user's email
     * @return List of GoogleAccountDto objects
     */
    List<GoogleAccountDto> getAccountsByToken(String token);
}
