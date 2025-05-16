package com.teqinvalley.project.user_crud.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("youtube-video-uploads")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class YoutubeVideoUpload {

    @Id
    private String id;
    private GoogleAccount googleAccountId;
    private String title;
    private String description;
    private String privacyStatus;
    private String filePath;
    private String uploadStatus; // Example: "Pending", "Uploaded"
    private String youtubeVideoId;
}
