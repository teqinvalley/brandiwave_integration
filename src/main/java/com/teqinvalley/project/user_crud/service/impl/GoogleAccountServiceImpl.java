package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.GoogleAccountDto;
import com.teqinvalley.project.user_crud.model.GoogleAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.GoogleAccountRepository;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.GoogleAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GoogleAccountServiceImpl implements GoogleAccountService {

    @Autowired
    private GoogleAccountRepository googleAccountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves Google account information received from OAuth response.
     */
    @Override
    public void savePages(List<Map<String, Object>> pages, String accessToken, String userEmail) {
        for (Map<String, Object> page : pages) {
            GoogleAccount entity = new GoogleAccount();
            entity.setName((String) page.get("name"));
            entity.setGoogleId((String) page.get("id"));
            entity.setToken((String) page.get("access_token"));
            entity.setEmail(userEmail);
            googleAccountRepository.save(entity);
        }
    }

    /**
     * Retrieves Google accounts linked to a user identified via the JWT token.
     */
    @Override
    public List<GoogleAccountDto> getAccountsByToken(String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtUtil.extractUsername(token);
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<GoogleAccount> accounts = googleAccountRepository.findByUser(user);

        return accounts.stream()
                .map(acc -> new GoogleAccountDto(acc.getGoogleId(), acc.getName()))
                .collect(Collectors.toList());
    }
}

