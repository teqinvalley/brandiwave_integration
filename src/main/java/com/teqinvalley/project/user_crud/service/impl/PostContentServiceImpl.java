package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.PostContentRequestDto;
import com.teqinvalley.project.user_crud.model.PostContent;
import com.teqinvalley.project.user_crud.repository.PostContentRepository;
import com.teqinvalley.project.user_crud.service.PostContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostContentServiceImpl implements PostContentService {

    @Autowired
    private PostContentRepository postContentRepo;
    @Override
    public PostContent createContent(PostContentRequestDto postDto) {

        PostContent content = new PostContent();
        content.setContentHtml(postDto.getContentHtml());
        content.setCreatedAt(LocalDateTime.now());
        content.setUpdatedAt(LocalDateTime.now());

        return postContentRepo.save(content);
    }
}
