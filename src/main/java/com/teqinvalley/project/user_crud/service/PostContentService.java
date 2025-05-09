package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.PostContentRequestDto;
import com.teqinvalley.project.user_crud.model.PostContent;

public interface PostContentService {

    PostContent createContent(PostContentRequestDto postDto);
}

