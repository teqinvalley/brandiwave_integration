package com.teqinvalley.project.user_crud.model;

import lombok.Data;

import java.util.List;

@Data
public class PlatformContent {

    private  String platform;
    private List<String> accountIds;
    private String textContent;
    private String imageUrl;
    private String videoUrl;
}
