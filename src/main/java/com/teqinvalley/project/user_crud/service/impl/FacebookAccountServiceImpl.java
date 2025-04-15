package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.model.FacebookAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.FacebookAccountRepository;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.IFacebookAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacebookAccountServiceImpl implements IFacebookAccountService {

    @Autowired
    private FacebookAccountRepository facebookAccountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<FacebookAccountDto> getAccountsByToken(String token) {
        // Remove "Bearer " if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtUtil.extractUsername(token);
        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<FacebookAccount> accounts = facebookAccountRepository.findByUser(user);

        return accounts.stream()
                .map(acc -> new FacebookAccountDto(acc.getFacebookId(), acc.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public void savePages(List<Map<String, Object>> pages, String accessToken, String email) {
        for (Map<String, Object> page : pages) {
            FacebookAccount entity = new FacebookAccount();
            entity.setName((String) page.get("name"));
            entity.setFacebookId((String) page.get("id"));
            entity.setToken((String) page.get("access_token"));
            entity.setEmail(email);

            facebookAccountRepository.save(entity);
        }
    }

}

