package com.teqinvalley.project.user_crud.model;

import com.teqinvalley.project.user_crud.enumeration.MyProfileDesignation;
import com.teqinvalley.project.user_crud.enumeration.MyProfileRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Document(collection = "user_profile")
public class MyProfile {

    @Id
    private String id;
    @NotNull(message = "Full Name is Required")
    private String fullName;
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private Long mobileNumber;

    private MyProfileRole role;

    private MyProfileDesignation designation;

    private boolean completedProfile;

    private String profilePictureUrl;
    @Size(max = 100, message = "Signature should be max 100 characters")
    private String signature;
}
