package com.teqinvalley.project.user_crud.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Customize_Platform")
@Data
public class CustomizePlatform {

    @Id
    private ObjectId id;
    private String title;
    private List<PlatformContent> platformContent;
}
