package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.YoutubeVideoUploadDto;
import com.teqinvalley.project.user_crud.service.GoogleAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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
        private JwtUtil jwtUtil;

        @Autowired
        private GoogleAccountService googleAccountService;

        @Value("${google.app.id}")
        private String googleAppId;

        @Value("${google.app.secret}")
        private String googleAppSecret;

        @Value("${google.redirect.uri}")
        private String redirectUri;

        /**
         * Redirects the user to Google's OAuth 2.0 authorization endpoint.
         */
        @GetMapping("/login")
        public void redirectToGoogle(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
            System.out.println("Inside login");

            String googleOAuthUrl = "https://accounts.google.com/o/oauth2/auth"
                    + "?client_id=" + googleAppId
                    + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                    + "&scope=" + URLEncoder.encode(
                    "https://www.googleapis.com/auth/youtube.force-ssl "
                            + "https://www.googleapis.com/auth/youtube "
                            + "https://www.googleapis.com/auth/youtubepartner "
                            + "https://www.googleapis.com/auth/youtube.channel-memberships.creator "
                            + "https://www.googleapis.com/auth/youtube.upload "
                            + "https://www.googleapis.com/auth/youtube.readonly", StandardCharsets.UTF_8)
                    + "&response_type=code"
                    + "&state=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

            System.out.println("Redirect URI: " + redirectUri);
            System.out.println("DEBUG >>> Google Login URL: " + googleOAuthUrl);

            response.sendRedirect(googleOAuthUrl);
        }

        /**
         * Handles the OAuth 2.0 callback and retrieves access token.
         */
        @GetMapping("/callback")
        public void handleGoogleCallback(
                @RequestParam("code") String code,
                @RequestParam("state") String stateToken,
                HttpServletResponse response
        ) throws IOException {

            String tokenUrl = "https://oauth2.googleapis.com/token"
                    + "?client_id=" + googleAppId
                    + "&redirect_uri=" + redirectUri
                    + "&client_secret=" + googleAppSecret
                    + "&code=" + code
                    + "&grant_type=authorization_code";

            System.out.println("Token URL: " + tokenUrl);

            RestTemplate restTemplate = new RestTemplate();

            try {
                System.out.println("Inside try block");

                // Step 1: Get Access Token
                Map<String, Object> tokenResponse = restTemplate.getForObject(tokenUrl, Map.class);
                System.out.println("Access Token Response: " + tokenResponse);

                if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                    System.out.println("Access token not found in response");
                    return;
                }

                String accessToken = (String) tokenResponse.get("access_token");
                System.out.println("Access Token: " + accessToken);

                // Step 2: Dummy call to illustrate next step (No actual "pages" for YouTube)
                // This can be updated to fetch channel info, etc.
                String dummyUrl = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
                Map<String, Object> userInfo = restTemplate.getForObject(dummyUrl, Map.class);
                System.out.println("User Info Response: " + userInfo);

                String email = jwtUtil.extractUsername(stateToken);
                System.out.println("Email from JWT token: " + email);

                googleAccountService.savePages(List.of(userInfo), accessToken, email);

                response.sendRedirect("http://127.0.0.1:5500/createpost1.html");

            } catch (HttpClientErrorException e) {
                System.out.println("Google API error: " + e.getResponseBodyAsString());
                e.printStackTrace();
            }
        }

        /**
         * Uploads a video to YouTube using the resumable upload protocol.
         */
        @PostMapping("/youtube-upload")
        public ResponseEntity<String> uploadVideo(
                @RequestParam("accessToken") String accessToken,
//                @RequestParam("filePath") String filePath,
                @RequestBody YoutubeVideoUploadDto videoUploadDto
        ) {
            try {
                File videoFile = new File(videoUploadDto.getFilePath());
                byte[] videoBytes = Files.readAllBytes(videoFile.toPath());

                // Step 1: Initiate upload session
                String uploadUrl = "https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status";

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + accessToken);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Upload-Content-Type", "video/mp4");
                headers.set("X-Upload-Content-Length", String.valueOf(videoFile.length()));

                String requestBody = "{ \"snippet\": { \"title\": \"" + videoUploadDto.getTitle() + "\", "
                        + "\"description\": \"" + videoUploadDto.getDescription() + "\" }, "
                        + "\"status\": { \"privacyStatus\": \"" + videoUploadDto.getPrivacyStatus() + "\" }}";

                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                RestTemplate restTemplate = new RestTemplate();

                ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);
                String uploadSessionUrl = response.getHeaders().getLocation().toString();

                if (uploadSessionUrl == null || uploadSessionUrl.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload session URL is missing");
                }

                // Step 2: Upload the video file to the session URL
                HttpHeaders uploadHeaders = new HttpHeaders();
                uploadHeaders.set("Authorization", "Bearer " + accessToken);
                uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                uploadHeaders.set("Content-Length", String.valueOf(videoBytes.length));

                HttpEntity<byte[]> uploadEntity = new HttpEntity<>(videoBytes, uploadHeaders);
                ResponseEntity<String> uploadResponse = restTemplate.exchange(uploadSessionUrl, HttpMethod.PUT, uploadEntity, String.class);

                return ResponseEntity.ok("Video uploaded successfully: " + uploadResponse.getBody());

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading video file: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video: " + e.getMessage());
            }
        }
    }


