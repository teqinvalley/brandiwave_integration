package com.teqinvalley.project.user_crud.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tbl_user_info")
public class UserModel {
    @Id
    private String id;

    @Field("email_address")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;


    @Field("organisation_name")
    private String organisationName;

    @Field("full_name")
    private String fullName;

    @Field("mobile_number")
    private Long mobileNumber;

    @Field("password")

    private String password;

    @Field("completed_profile")
    private boolean completedProfile;

    @Field("created_at")
    @CreatedDate
    private Date createdAt;

}