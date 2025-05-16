package com.teqinvalley.project.user_crud.controller;

import ch.qos.logback.core.CoreConstants;
import com.teqinvalley.project.user_crud.dto.request.YoutubeVideoUploadDto;
import com.teqinvalley.project.user_crud.service.IGoogleAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/google")
@CrossOrigin
public class GoogleAccountController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private IGoogleAccountService googleAccountService;


    @GetMapping("/login")
    public void redirectToGoogle(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
       googleAccountService.redirectToGoogleOAuthPage(token,response);
    }

    @GetMapping("/callback")
    public void handleGoogleCallback(@RequestParam("code") String code, @RequestParam("state") String stateToken,  // here’s your JWT from frontend
            HttpServletResponse response) throws IOException {
            googleAccountService.handleGoogleCallback(code,stateToken,response);
    }

    @PostMapping("/youtube-upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("accessToken") String accessToken, @RequestBody YoutubeVideoUploadDto videoUploadDto) {
            return googleAccountService.uploadVideo(accessToken,videoUploadDto);
        }

}
