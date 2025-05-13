package com.teqinvalley.project.user_crud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Document(collection="google_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccount {
    @Id
    private String id;

    private String googleId; // ✅ Must be present

    private String name;

    private UserModel user;

    private String email;

    private String token;// Or String userId if you're saving user manually

}
