package com.teqinvalley.project.user_crud.service;
//created by me
public interface ITokenBlacklistService {
    void blacklistToken(String token);
    boolean isTokenBlacklisted(String token);
}
