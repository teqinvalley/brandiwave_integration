package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.TwitterAccountDto;
import com.teqinvalley.project.user_crud.model.TwitterAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.TwitterAccountRepository;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.ITwitterAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TwitterAccountServiceImpl implements ITwitterAccountService {

    @Autowired
    private TwitterAccountRepository twitterAccountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TwitterAccountDto> getAccountsByToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String email = jwtUtil.extractUsername(token);
        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<TwitterAccount> accounts = twitterAccountRepository.findByUser(user);

        return accounts.stream()
                .map(acc -> new TwitterAccountDto(acc.getTwitterId(), acc.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void saveAccount(Map<String, Object> twitterUserData, String accessToken, String email) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        TwitterAccount entity = new TwitterAccount();
        entity.setTwitterId((String) twitterUserData.get("id"));
        entity.setName((String) twitterUserData.get("name"));
        entity.setEmail(email);
        entity.setToken(accessToken);
        entity.setUser(user);

        twitterAccountRepository.save(entity);
    }
}