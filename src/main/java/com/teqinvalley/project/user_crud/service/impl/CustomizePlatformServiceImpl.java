package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.CustomizePlatformDto;
import com.teqinvalley.project.user_crud.dto.request.PlatformContentDto;
import com.teqinvalley.project.user_crud.model.CustomizePlatform;
import com.teqinvalley.project.user_crud.model.PlatformContent;
import com.teqinvalley.project.user_crud.repository.CustomizePlatformRepository;
import com.teqinvalley.project.user_crud.service.CustomizePlatformService;
import com.teqinvalley.project.user_crud.utils.PlatformContentValidator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class CustomizePlatformServiceImpl implements CustomizePlatformService {

    @Autowired
    private CustomizePlatformRepository customizePlatformRepo;

    @Override
    public CustomizePlatform createPost(CustomizePlatformDto customizeDto) {

        List<PlatformContent> content = new ArrayList<>();
        for(PlatformContentDto dto : customizeDto.getPlatformContent()){
            PlatformContentValidator.validatePlatform(dto);

            PlatformContent contents = new PlatformContent();
            contents.setPlatform(dto.getPlatform());
            contents.setAccountIds(dto.getAccountIds());
            contents.setTextContent(dto.getTextContent());
            contents.setImageUrl(dto.getImageUrl());
            contents.setVideoUrl(dto.getVideoUrl());

            content.add(contents);

        }
        CustomizePlatform platform = new CustomizePlatform();
        platform.setTitle(customizeDto.getTitle());
        platform.setPlatformContent(content);

        return customizePlatformRepo.save(platform);
    }

    @Override
    public List<PlatformContent> getPostPreview(String id) {
        Optional<CustomizePlatform> post = customizePlatformRepo.findById(new ObjectId(id));
        if (post.isPresent()){
            return  post.get().getPlatformContent();
        }
        else {
            throw  new NoSuchElementException("post not found with ID:" +id);
        }

    }
}
