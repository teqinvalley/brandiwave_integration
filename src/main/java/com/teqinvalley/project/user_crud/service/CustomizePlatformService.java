package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.CustomizePlatformDto;
import com.teqinvalley.project.user_crud.model.CustomizePlatform;
import com.teqinvalley.project.user_crud.model.PlatformContent;

import java.util.List;

public interface CustomizePlatformService {

    CustomizePlatform createPost(CustomizePlatformDto customizeDto);
    List<PlatformContent> getPostPreview(String id);
}
