package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.model.MyProfile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MyProfileService {

    MyProfile getProfileByEmail(String email);
    MyProfile updateProfile(String email, MyProfile update);
    void uploadProfilePicture(String email, MultipartFile image) throws IOException;
    void uploadSignature(String email, MultipartFile signature);
    void deleteProfile(String email);
}
