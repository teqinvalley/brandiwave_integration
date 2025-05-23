package com.teqinvalley.project.user_crud.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "youtubeVideo-engagementDetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class YoutubeVideoEngagementDetails {

        private String id;
        private YoutubeVideoUpload videoId;
        private int viewCount;
        private int likeCount;
        private int commentCount;
        private int shareCount;
        private LocalDateTime timestamp; // Store engagement data with timestamp
}
