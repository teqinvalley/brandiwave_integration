package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.service.IFacebookAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/facebook")
@CrossOrigin
public class FacebookAccountController {

    @Autowired
    private IFacebookAccountService facebookAccountService;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${facebook.app.id}")
    private String facebookAppId;

    @Value("${facebook.app.secret}")
    private String facebookAppSecret;

    @Value("${facebook.redirect.uri}")
    private String redirectUri;

    @GetMapping("/login")
    public void redirectToFacebook(@RequestParam("token") String token, HttpServletResponse response) throws IOException {

        System.out.println("inside login");
        String facebookOAuthUrl = "https://www.facebook.com/v18.0/dialog/oauth" +
                "?client_id=" + facebookAppId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope=pages_show_list,pages_read_engagement,pages_manage_posts" +
                "&response_type=code" +
                "&state=" + URLEncoder.encode(token, StandardCharsets.UTF_8); // ✅ include JWT as 'state'
        System.out.println("red uri"+redirectUri);
        System.out.println("DEBUG >>> Facebook Login URL: " + facebookOAuthUrl);
        response.sendRedirect(facebookOAuthUrl);
    }


    @GetMapping("/callback")
    public void handleFacebookCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String stateToken,  // here’s your JWT from frontend
            HttpServletResponse response
    ) throws IOException {

        String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token" +
                "?client_id=" + facebookAppId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&client_secret=" + facebookAppSecret +
                "&code=" + code;

        System.out.println("token" + tokenUrl);

        System.out.println("rediecturi"+redirectUri);
        RestTemplate restTemplate = new RestTemplate();
        try {
            // STEP 1: Get access token
            System.out.println("inside try ---------------------------->>>");
            Map<String, Object> tokenResponse = restTemplate.getForObject(tokenUrl, Map.class);
            System.out.println("getting response--------------------------->>>>>>>>..");
            System.out.println("Access Token Response: " + tokenResponse);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                System.out.println("Access token not found in response");
                return;
            }

            String accessToken = (String) tokenResponse.get("access_token");
            System.out.println("Access Token: " + accessToken);

            // STEP 2: Get Pages using access token
            String pagesUrl = "https://graph.facebook.com/me/accounts?access_token=" + accessToken;
            Map<String, Object> pagesResponse = restTemplate.getForObject(pagesUrl, Map.class);

            System.out.println("Pages response: " + pagesResponse);

            List<Map<String, Object>> pages = (List<Map<String, Object>>) pagesResponse.get("data");

            String email = jwtUtil.extractUsername(stateToken);
            System.out.println("Email from token: " + email);

            facebookAccountService.savePages(pages, accessToken, email);

            response.sendRedirect("http://127.0.0.1:5500/createpost1.html");


        } catch (HttpClientErrorException e) {
            System.out.println("Facebook API error: " + e.getResponseBodyAsString());
            e.printStackTrace();
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<FacebookAccountDto>> getFacebookAccounts(@RequestHeader("Authorization") String token) {
        List<FacebookAccountDto> accounts = facebookAccountService.getAccountsByToken(token);
        return ResponseEntity.ok(accounts);
    }
}

