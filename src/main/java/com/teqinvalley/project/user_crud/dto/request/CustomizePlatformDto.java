package com.teqinvalley.project.user_crud.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CustomizePlatformDto {

    private  String title;
    private List<PlatformContentDto> platformContent;
}
