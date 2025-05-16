package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.dto.request.GoogleAccountDto;
import com.teqinvalley.project.user_crud.dto.request.YoutubeVideoUploadDto;
import com.teqinvalley.project.user_crud.model.FacebookAccount;
import com.teqinvalley.project.user_crud.model.GoogleAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.model.YoutubeVideoUpload;
import com.teqinvalley.project.user_crud.repository.FacebookAccountRepository;
import com.teqinvalley.project.user_crud.repository.GoogleAccountRepository;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.repository.YoutubeUploadRepository;
import com.teqinvalley.project.user_crud.service.IGoogleAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
import java.util.stream.Collectors;

@Service
public class GoogleAccountServiceImpl implements IGoogleAccountService {

    @Autowired
    private GoogleAccountRepository googleAccountRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YoutubeUploadRepository youtubeUploadRepository;

    @Value("${google.app.id}")
    private String googleAppId;

    @Value("${google.app.secret}")
    private String googleAppSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;


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

    @Override
    public List<GoogleAccountDto> getAccountsByToken(String token) {
        // Remove "Bearer " if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String email = jwtUtil.extractUsername(token);
        UserModel user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<GoogleAccount> accounts = googleAccountRepository.findByUser(user);

        return accounts.stream()
                .map(acc -> new GoogleAccountDto(acc.getGoogleId(), acc.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> uploadVideo(String accessToken, YoutubeVideoUploadDto videoUploadDto) {
        try {

            System.out.println("...started getting google data");
            Map<String,Object> googleData=getGoogleAccountDetails(accessToken);
            System.out.println("Google Info "+googleData);
            YoutubeVideoUpload videoUpload=new YoutubeVideoUpload();
            GoogleAccount account=new GoogleAccount();
            account.setGoogleId((String) googleData.get("sub"));
            account.setName((String) googleData.get("name"));
            account.setEmail((String) googleData.get("email"));
            videoUpload.setGoogleAccountId(account);
            videoUpload.setUploadStatus("Pending");
            videoUpload.setDescription(videoUploadDto.getDescription());
            videoUpload.setTitle(videoUploadDto.getTitle());
            videoUpload.setFilePath(videoUploadDto.getFilePath());
            videoUpload.setPrivacyStatus(videoUploadDto.getPrivacyStatus());
            YoutubeVideoUpload videouploaded=youtubeUploadRepository.save(videoUpload);

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

            videouploaded.setUploadStatus("Uploaded");
            youtubeUploadRepository.save(videouploaded);
            return ResponseEntity.ok("Video uploaded successfully: " + response.getBody());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading video file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload video: " + e.getMessage());
        }
    }

    @Override
    public void redirectToGoogleOAuthPage(String token, HttpServletResponse response) throws IOException{
        System.out.println("inside login");
        String googleOAuthUrl = "https://accounts.google.com/o/oauth2/auth" +
                "?client_id=" + googleAppId +
                "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8) +
                "&scope="+ URLEncoder.encode("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/youtube.force-ssl https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/youtubepartner https://www.googleapis.com/auth/youtube.channel-memberships.creator https://www.googleapis.com/auth/youtube.upload https://www.googleapis.com/auth/youtube.readonly", StandardCharsets.UTF_8) +
                "&response_type=code" +
                "&state=" + URLEncoder.encode(token, StandardCharsets.UTF_8); // ✅ include JWT as 'state'
        System.out.println("red uri"+redirectUri);
        System.out.println("DEBUG >>> Google Login URL: " + googleOAuthUrl);
        response.sendRedirect(googleOAuthUrl);
    }

    @Override
    public void handleGoogleCallback(String code, String stateToken, HttpServletResponse response) throws IOException {

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
            savePages(pages, accessToken, email);
            response.sendRedirect("http://127.0.0.1:5500/createpost1.html");

        } catch (HttpClientErrorException e) {
            System.out.println("Google API error: " + e.getResponseBodyAsString());
            e.printStackTrace();
        }



    }

    private Map<String, Object> getGoogleAccountDetails(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        System.out.println("google info response " + response.getBody());

        return response.getBody();
    }

}
