package com.teqinvalley.project.user_crud.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class FacebookEngagementResponse {

    private String viewCount;
    private String likeCount;
    private String commentCount;
    private String shareCount;
    private String followerCount;
    private String videosCount;
    private String pageCount;
    private String postCount;
}
