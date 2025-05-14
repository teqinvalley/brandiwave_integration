package com.teqinvalley.project.user_crud.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class PlatformContentDto {
    private String platform;
    private List<String> accountIds;
    private String textContent;
    private String imageUrl;
    private String videoUrl;

}
