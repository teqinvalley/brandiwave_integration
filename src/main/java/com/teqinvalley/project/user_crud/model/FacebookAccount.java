package com.teqinvalley.project.user_crud.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "facebook_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacebookAccount {

    @Id
    private String id;

    private String facebookId; // ✅ Must be present

    private String name;

    private UserModel user;

    private String email;

    private String token;// Or String userId if you're saving user manually
}
