package com.teqinvalley.project.user_crud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teqinvalley.project.user_crud.dto.request.FacebookAccountDto;
import com.teqinvalley.project.user_crud.dto.request.UserEngagementRequestDto;
import com.teqinvalley.project.user_crud.dto.response.FacebookEngagementResponse;
import com.teqinvalley.project.user_crud.model.FacebookAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.FacebookAccountRepository;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.IFacebookAccountService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public Map<String, Object> getFacebookEngagement(UserEngagementRequestDto userEngagementRequestDto) throws  Exception{
        String GRAPH_API_URL = "https://graph.facebook.com/me/posts?fields=id,created_time&since=" + userEngagementRequestDto.getStartDate() + "&until=" + userEngagementRequestDto.getEndDate() + "&access_token=" + userEngagementRequestDto.getFacebookAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(GRAPH_API_URL, String.class);
        List<String> postId=extractPostIds(response);
        Map<String, Integer> totalEngagement = getTotalEngagement(postId,userEngagementRequestDto.getFacebookAccessToken());
        Map<String, Object> responseData = new HashMap<>();
        FacebookEngagementResponse facebookData = new FacebookEngagementResponse();
        facebookData=getTotalFollowersCount(userEngagementRequestDto,facebookData);
        facebookData.setLikeCount(String.valueOf(totalEngagement.get("likes")));
        facebookData.setShareCount(String.valueOf(totalEngagement.get("shares")));
        facebookData.setCommentCount(String.valueOf(totalEngagement.get("comments")));
        facebookData.setPostCount(String.valueOf(totalEngagement.get("postCount")));

        responseData.put("Facebook", facebookData);
        return responseData;
    }


    public FacebookEngagementResponse getTotalFollowersCount(UserEngagementRequestDto userEngagementRequestDto,FacebookEngagementResponse facebookData) throws JsonProcessingException {
        String GRAPH_API_URL = "https://graph.facebook.com/v19.0/me/accounts?fields=id,name,access_token&access_token=" + userEngagementRequestDto.getFacebookPageAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(GRAPH_API_URL, String.class);
        System.out.println("page response "+response);
        String pageAccessToken=getPageAccessToken(response);
        System.out.println("page access token  "+pageAccessToken);
        List<String> pageIds = extractPostIds(response);
        int totalFollowers = 0;
        int totalview=0;
        for (String pageId : pageIds) {
            totalFollowers += extractFollowerCount(pageId,pageAccessToken,userEngagementRequestDto);
            // to get total view under a page
            totalview = totalview + getTotalViewsPerPage(pageId,pageAccessToken, userEngagementRequestDto);
        }
        facebookData.setPageCount(String.valueOf(pageIds.size()));
        facebookData.setFollowerCount(String.valueOf(totalFollowers));
        facebookData.setViewCount(String.valueOf(totalview));
        return facebookData;
    }

    private String getPageAccessToken(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        JsonNode dataArray = rootNode.path("data");

        String pageAccessToken = "";
        if (dataArray.isArray() && dataArray.size() > 0) {
            pageAccessToken = dataArray.get(0).path("access_token").asText(); // Get the page access token
        }
        return pageAccessToken;
    }

    private int getTotalViewsPerPage(String pageId,String pageAccessToken, UserEngagementRequestDto userEngagementRequestDto) {
            RestTemplate restTemplate = new RestTemplate();
//            String url = "https://graph.facebook.com/" + pageId + "/insights/page_views_total?since="
//                    + userEngagementRequestDto.getStartDate() + "&until="
//                    + userEngagementRequestDto.getEndDate() + "&access_token="
//                    + pageAccessToken;
       String url="https://graph.facebook.com/v22.0/"+pageId+"/insights?metric=page_impressions_unique&period=day&access_token="+pageAccessToken;

        try {
                String response = restTemplate.getForObject(url, String.class);
                System.out.println("View and follow response "+response);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response);

                JsonNode dataArray = rootNode.path("data"); // Ensure correct parsing
                if (dataArray.isArray() && dataArray.size() > 0) {
                    return dataArray.get(0).path("values").path(0).path("value").asInt(0);
                }
            } catch (Exception e) {
                System.err.println("Error fetching page views: " + e.getMessage());
            }
            return 0;

    }

    private int extractFollowerCount(String pageId,String pageAccessToken,UserEngagementRequestDto userEngagementRequestDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://graph.facebook.com/"+pageId+"?fields=fan_count&period=day&since=" + userEngagementRequestDto.getStartDate() + "&until=" + userEngagementRequestDto.getEndDate() + "&access_token="+pageAccessToken ;
        String pageResponse = restTemplate.getForObject(url, String.class);
        JsonNode rootNode = objectMapper.readTree(pageResponse);
        int fanCount = rootNode.path("fan_count").asInt();
        return fanCount;
    }

    private List<String> extractPostIds(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        JsonNode dataArray = rootNode.get("data");

        List<String> postIds = new ArrayList<>();
        for (JsonNode post : dataArray) {
            postIds.add(post.get("id").asText());
        }

        return postIds;
    }

    private Map<String, Integer> getTotalEngagement(List<String> postIds,String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Integer> engagementCounts = new HashMap<>();
        for (String postId : postIds) {
            if (postId == null || postId.isEmpty()) {
                continue; // Skip invalid post IDs
            }

            String url = "https://graph.facebook.com/" + postId + "?fields=reactions.summary(true),comments.summary(true),shares&access_token=" + token;
            String response = restTemplate.getForObject(url, String.class);
            // Parse JSON and aggregate counts
            updateEngagementCounts(response, engagementCounts);
            engagementCounts.put("postCount", engagementCounts.getOrDefault("postCount", 0) + 1);

        }

        return engagementCounts;
    }

    private void updateEngagementCounts(String response, Map<String, Integer> engagementCounts) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);
        int likes = rootNode.path("reactions").path("summary").path("total_count").asInt(0);
        int comments = rootNode.path("comments").path("summary").path("total_count").asInt(0);
        int shares = rootNode.path("shares").path("count").asInt(0); // Handles missing shares field

        engagementCounts.put("likes", engagementCounts.getOrDefault("likes", 0) + likes);
        engagementCounts.put("comments", engagementCounts.getOrDefault("comments", 0) + comments);
        engagementCounts.put("shares", engagementCounts.getOrDefault("shares", 0) + shares);

    }


}

