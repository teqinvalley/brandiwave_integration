package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.ScheduleRequestDto;
import com.teqinvalley.project.user_crud.model.ScheduledPosts;
import com.teqinvalley.project.user_crud.repository.ScheduledPostRepository;
import com.teqinvalley.project.user_crud.service.ScheduledPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.*;
import java.util.List;

@Service
public class ScheduledPostServiceImpl implements ScheduledPostService {

    @Autowired
    private ScheduledPostRepository postRepository;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ScheduledPostServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public ScheduledPosts schedule(String platform, ScheduleRequestDto request) {

        ScheduledPosts post = new ScheduledPosts();
        post.setPlatform(platform);
        post.setTimeZone(request.getTimeZone());


        ZoneId zone = ZoneId.of(request.getTimeZone()); // e.g. "Asia/Kolkata"
        LocalDateTime localtime = LocalDateTime.parse(request.getScheduledTime()); // e.g. "2025-05-09T14:30:00"
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localtime, zone);
        LocalDateTime utcTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();

        post.setScheduledTime(utcTime);
        post.setTimeZone(request.getTimeZone());
        return postRepository.save(post);
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void checkAndPost() {

        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        List<ScheduledPosts> duePosts = postRepository.findByPostedFalseAndScheduledTimeBefore(nowUtc);

        for(ScheduledPosts posts: duePosts){
            postToPlatform(posts);
            posts.setPosted(true);
            postRepository.save(posts);
        }
    }

    public void postToPlatform(ScheduledPosts post){
    String endpoint = null;

    switch (post.getPlatform().toLowerCase()){
        case"facebook":
            endpoint = "http://localhost:9093/userModule/facebook/login";
            break;
        case "instagram":
            endpoint = "http://localhost:9093/userModule/instagram/login";
            break;
        case "youTube":
            endpoint = "http://localhost:9093/userModule/youTube";
            break;
        default:
            System.out.println("unsupported platform:" +post.getPlatform());
            return;
    }
    try {
        webClientBuilder.build()
                .post().uri(endpoint)
                .retrieve().bodyToMono(String.class)
                .subscribe(response -> {
                    System.out.println(" Post successful to " + post.getPlatform() + ": " + response);
                }, error -> {
                    System.err.println(" Post failed to " + post.getPlatform() + ": " + error.getMessage());
                });

    }
    catch (Exception e){
        System.err.println("Exception posting to " +post.getPlatform() + ":" +e.getMessage());
    }

        System.out.println("Posting to: " + post.getPlatform().toUpperCase() +
                " | Scheduled UTC Time: " + post.getScheduledTime() +
                " | Original Timezone: " + post.getTimeZone());
    }
}
