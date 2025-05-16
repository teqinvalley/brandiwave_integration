package com.teqinvalley.project.user_crud.dto.request;

import lombok.Data;

@Data
public class ScheduleRequestDto {

    private  String timeZone;
    private String scheduledTime;
    private String title;
    private String description;
    private String privacyStatus;
    private String filePath;
}
