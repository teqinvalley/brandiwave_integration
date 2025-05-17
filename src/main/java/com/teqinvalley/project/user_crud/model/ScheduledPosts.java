package com.teqinvalley.project.user_crud.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "scheduled_posts")
@Data
public class ScheduledPosts {

    @Id
    private String id;
    private String platform;
    private String timeZone;
    private LocalDateTime scheduledTime;
    private  boolean posted = false;
    private String title;                // for YouTube
    private String description;          // for YouTube or optional caption for Instagram
    private String privacyStatus;        // for YouTube: public, private, unlisted

    // Media fields
//    private String imageUrl;             // used by Facebook, Instagram
//    private String videoUrl;             // hosted video URL for Facebook, Instagram
    private String filePath;             // for local video file path (used by YouTube)

    // OAuth token
    private String accessToken;
}
