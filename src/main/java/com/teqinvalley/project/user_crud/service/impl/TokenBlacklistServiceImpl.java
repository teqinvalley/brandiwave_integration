package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.service.ITokenBlacklistService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
// created by me
@Service
public class TokenBlacklistServiceImpl implements ITokenBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Override
    public void blacklistToken(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
