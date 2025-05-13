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

    @Value("${google.app.id}")
    private String googleAppId;

    @Value("${google.app.secret}")
    private String googleAppSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Autowired
    private IGoogleAccountService googleAccountService;


    @GetMapping("/login")
    public void redirectToGoogle(@RequestParam("token") String token, HttpServletResponse response) throws IOException {

        System.out.println("inside login");
        String googleOAuthUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + googleAppId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope="+ URLEncoder.encode("https://www.googleapis.com/auth/youtube.force-ssl https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtubepartner https://www.googleapis.com/auth/youtube.channel-memberships.creator https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtube.readonly", StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&state=" + URLEncoder.encode(token, StandardCharsets.UTF_8); // ✅ include JWT as 'state'
        System.out.println("red uri"+redirectUri);
        System.out.println("DEBUG >>> Google Login URL: " + googleOAuthUrl);
        response.sendRedirect(googleOAuthUrl);

    }


    @GetMapping("/callback")
    public void handleGoogleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String stateToken,  // here’s your JWT from frontend
            HttpServletResponse response
    ) throws IOException {


        String tokenUrl = "https://oauth2.googleapis.com/token" +
                "?client_id=" + googleAppId +
                "&redirect_uri=" + redirectUri+
                "&client_secret=" + googleAppSecret +
                "&code=" + code;

        System.out.println("token" + tokenUrl);

        System.out.println("redirecturi"+redirectUri);
        RestTemplate restTemplate = new RestTemplate();
        try {
            // STEP 1: Get access token
            System.out.println("inside try ---------------------------->>>");
            Map<String, Object> tokenResponse = restTemplate.getForObject(tokenUrl, Map.class);

            System.out.println("Access Token Response: " + tokenResponse);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                System.out.println("Access token not found in response");
                return;
            }

            String accessToken = (String) tokenResponse.get("access_token");
            System.out.println("Access Token: " + accessToken);

            // STEP 2: Get Pages using access token
            String pagesUrl = "https://oauth2.googleapis.com/token" + accessToken;
            Map<String, Object> pagesResponse = restTemplate.getForObject(pagesUrl, Map.class);

            System.out.println("Pages response: " + pagesResponse);

            List<Map<String, Object>> pages = (List<Map<String, Object>>) pagesResponse.get("data");

            String email = jwtUtil.extractUsername(stateToken);
            System.out.println("Email from token: " + email);
            googleAccountService.savePages(pages, accessToken, email);
            response.sendRedirect("http://127.0.0.1:5500/createpost1.html");

        } catch (HttpClientErrorException e) {
            System.out.println("Google API error: " + e.getResponseBodyAsString());
            e.printStackTrace();
        }



    }


    @PostMapping("/youtube-upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("accessToken") String accessToken, @RequestBody YoutubeVideoUploadDto videoUploadDto) {
            try {

                System.out.println("...started"+videoUploadDto.getFilePath());

                File videoFile = new File(videoUploadDto.getFilePath());
                byte[] videoBytes = Files.readAllBytes(videoFile.toPath());
                String uploadUrl = "https://www.googleapis.com/upload/youtube/v3/videos?uploadType=resumable&part=snippet,status";

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + accessToken);
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("X-Upload-Content-Type", "video/mp4"); // Specify file type
                headers.set("X-Upload-Content-Length", String.valueOf(videoFile.length())); // Specify file size

                // **Step 1: Initiate Upload Session**
                // ✅ Generate request body dynamically using DTO
                String requestBody = "{ \"snippet\": { \"title\": \"" + videoUploadDto.getTitle() + "\", " +
                        "\"description\": \"" + videoUploadDto.getDescription() + "\" }, " +
                        "\"status\": { \"privacyStatus\": \"" + videoUploadDto.getPrivacyStatus() + "\" }}";


                HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
                RestTemplate restTemplate = new RestTemplate();
                System.out.println("DEBUG >>> Request URL: " + uploadUrl);
                System.out.println("DEBUG >>> Request headers: " + requestBody);

                ResponseEntity<String> response = response=restTemplate.exchange(uploadUrl, HttpMethod.POST, entity, String.class);
                // ✅ Extract upload session URL from response headers
                String uploadSessionUrl = response.getHeaders().getLocation().toString();
                System.out.println("....."+uploadSessionUrl);
                HttpHeaders uploadHeaders = new HttpHeaders();
                uploadHeaders.set("Authorization", "Bearer " + accessToken);
                uploadHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                uploadHeaders.set("Content-Length", String.valueOf(videoBytes.length));

                if (uploadSessionUrl == null || uploadSessionUrl.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload session URL is missing");
                }

                HttpEntity<byte[]> uploadEntity = new HttpEntity<>(videoBytes, uploadHeaders);
                ResponseEntity<String> uploadResponse = restTemplate.exchange(uploadSessionUrl, HttpMethod.PUT, uploadEntity, String.class);
                System.out.println("DEBUG >>> Response Status: " + uploadResponse.getStatusCode());
                System.out.println("DEBUG >>> Response Body: " + uploadResponse.getBody());

                return ResponseEntity.ok("Video uploaded successfully: " + response.getBody());

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading video file: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video: " + e.getMessage());
            }
        }

}
