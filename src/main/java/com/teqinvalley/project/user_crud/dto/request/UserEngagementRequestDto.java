package com.teqinvalley.project.user_crud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEngagementRequestDto {
    private String googleAccessToken;
    private String facebookAccessToken;
    private String startDate;
    private String endDate;
    private String facebookPageAccessToken;
}
