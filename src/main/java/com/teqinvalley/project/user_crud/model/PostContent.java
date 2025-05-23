package com.teqinvalley.project.user_crud.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "content")
@Data
public class PostContent {

    @Id
    private String id;
    private String contentHtml;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

