package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.CustomizePlatformDto;
import com.teqinvalley.project.user_crud.model.CustomizePlatform;
import com.teqinvalley.project.user_crud.model.PlatformContent;
import com.teqinvalley.project.user_crud.service.CustomizePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/customize")
public class CustomizePlatformController {

    @Autowired
    private CustomizePlatformService service;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody CustomizePlatformDto customize){

        CustomizePlatform post = service.createPost(customize);

        return ResponseEntity.ok(post);

    }
    @GetMapping("/preview/{id}")
    public ResponseEntity<?> previewPost(@PathVariable("id") String id){
        try {
            List<PlatformContent> content = service.getPostPreview(id);
            return ResponseEntity.ok(content);
        }
        catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
