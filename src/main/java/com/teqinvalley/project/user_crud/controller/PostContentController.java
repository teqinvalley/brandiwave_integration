package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.PostContentRequestDto;
import com.teqinvalley.project.user_crud.model.PostContent;
import com.teqinvalley.project.user_crud.service.PostContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
public class PostContentController {

    @Autowired
    private PostContentService service;

    @PostMapping("/createContent")
    public ResponseEntity<PostContent> createContent(@RequestBody PostContentRequestDto Dto){

        PostContent content = service.createContent(Dto);

        return  ResponseEntity.ok(content);

    }
}
