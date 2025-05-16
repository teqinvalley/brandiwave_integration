package com.teqinvalley.project.user_crud.dto.request;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeVideoUploadDto {

    String title;
    String description;
    String privacyStatus;
    String filePath;
}
