package com.teqinvalley.project.user_crud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "google_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAccount {

    @Id
    private String id;

    private String googleId; // Required: Unique ID from Google

    private String name;

    private UserModel user; // Optional: Used for relational mapping if needed

    private String email;

    private String token; // Access token for Google API usage
}
