package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.TwitterAccountDto;
import com.teqinvalley.project.user_crud.service.ITwitterAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/twitter")
@CrossOrigin
public class TwitterAccountController {

    @Autowired
    private ITwitterAccountService twitterAccountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${twitter.api.key}")
    private String twitterApiKey;

    @Value("${twitter.api.secret}")
    private String twitterApiSecret;

    @Value("${twitter.redirect.uri}")
    private String redirectUri;

    @GetMapping("/login")
    public void redirectToTwitter(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        String twitterOAuthUrl = "https://api.twitter.com/oauth/authorize" +
                "?client_id=" + twitterApiKey +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&scope=tweet.read users.read follows.read offline.access" +
                "&state=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

        response.sendRedirect(twitterOAuthUrl);
    }

    @GetMapping("/callback")
    public void handleTwitterCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String stateToken,
            HttpServletResponse response
    ) throws IOException {

        String tokenUrl = "https://api.twitter.com/2/oauth2/token";

        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, String> requestBody = Map.of(
                    "code", code,
                    "client_id", twitterApiKey,
                    "client_secret", twitterApiSecret,
                    "redirect_uri", redirectUri,
                    "grant_type", "authorization_code"
            );

            Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, requestBody, Map.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                System.out.println("Access token not found in response");
                return;
            }

            String accessToken = (String) tokenResponse.get("access_token");
            String email = jwtUtil.extractUsername(stateToken);

            String userInfoUrl = "https://api.twitter.com/2/users/me?access_token=" + accessToken;
            Map<String, Object> twitterUserData = restTemplate.getForObject(userInfoUrl, Map.class);

            twitterAccountService.saveAccount(twitterUserData, accessToken, email);
            response.sendRedirect("http://127.0.0.1:5500/createpost1.html");

        } catch (Exception e) {
            System.out.println("Twitter API error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<TwitterAccountDto>> getTwitterAccounts(@RequestHeader("Authorization") String token) {
        List<TwitterAccountDto> accounts = twitterAccountService.getAccountsByToken(token);
        return ResponseEntity.ok(accounts);
    }
}
