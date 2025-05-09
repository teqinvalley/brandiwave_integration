package com.teqinvalley.project.user_crud.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "scheduled_posts")
@Data
public class ScheduledPosts {

    @Id
    private String Id;
    private String platform;
    private String timeZone;
    private LocalDateTime scheduledTime;
    private  boolean posted = false;
}
