package com.teqinvalley.project.user_crud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeVideoUploadDto {
    private String title;
    private String description;
    private String privacyStatus;
    private  String filePath;
}
