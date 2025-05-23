package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.config.MyProfileValidation;
import com.teqinvalley.project.user_crud.model.MyProfile;
import com.teqinvalley.project.user_crud.repository.MyProfileRepository;
import com.teqinvalley.project.user_crud.service.MyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class MyProfileServiceImpl implements MyProfileService {

    @Autowired
    private MyProfileRepository myProfileRepo;

    @Autowired
    private MyProfileValidation validation;


    @Override
    public MyProfile getProfileByEmail(String email) {

        return myProfileRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("profile not found"));
    }

    @Override
    public MyProfile updateProfile(String email, MyProfile update) {

        MyProfile exists = getProfileByEmail(email);

        if(!exists.getEmail().equals(update.getEmail())){
            throw  new RuntimeException("Email cannot be changed");
        }
        exists.setFullName(update.getFullName());
        exists.setRole(update.getRole());
        exists.setMobileNumber(update.getMobileNumber());
        exists.setProfilePictureUrl(update.getProfilePictureUrl());
        exists.setDesignation(update.getDesignation());

        return myProfileRepo.save(exists);
    }

    private String getFileExtension(String fileName){
        if(fileName!=null && fileName.contains(".")){
            return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        }
        return "";
    }
    private String getBaseName(String fileName) {
        if (fileName == null) return "";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) return fileName;
        return fileName.substring(0, dotIndex);
    }
    @Override
    public void uploadProfilePicture(String email, MultipartFile image) throws IOException {
        if(image.isEmpty()){
            throw  new RuntimeException("No file Uploaded");
//            String type
        }
        String extension = getFileExtension(image.getOriginalFilename());
//        if(!"jpg".equalsIgnoreCase(extension)){
//            throw  new IllegalArgumentException("Only JPG images allowed");
//        }
        if(!validation.getAllowedTypes().contains(extension.toLowerCase())){
            throw new IllegalArgumentException("only" +validation.getMaxSizeKb()+ "KB ALLOWED");
        }
//        if(image.getSize()>MAX_SIZE){
//            throw new IllegalArgumentException("File size exceeds 2MB");
//        }
        String base64 = Base64.getEncoder().encodeToString(image.getBytes());
        MyProfile profile = getProfileByEmail(email);
        profile.setProfilePictureUrl(base64);
        myProfileRepo.save(profile);
        System.out.println("Uploaded image size: " + image.getSize() + " bytes");



    }

    @Override
    public void uploadSignature(String email, MultipartFile signature) {
        try {
//            String content = new String(signature.getBytes());
//            if(content.length()>1000){
//                throw new RuntimeException("signature length too long");
//            }
            String extension = getFileExtension(signature.getOriginalFilename());
            long maxsizeBytes = validation.getMaxSizeKb() * 1024;
            if(signature.getSize()>maxsizeBytes){
                throw new RuntimeException("signature file exceeds:" +validation.getMaxSizeKb()+ "KB ALLOWED");
            }

            if(!validation.getAllowedTypes().contains(extension.toLowerCase())){
                throw new IllegalArgumentException("Only" +validation.getAllowedTypes()+ "files allowed");
            }

            String base64 = Base64.getEncoder().encodeToString(signature.getBytes());

            MyProfile profile = getProfileByEmail(email);
            profile.setSignature(base64);
            myProfileRepo.save(profile);

        } catch (IOException e) {
            throw new RuntimeException("Error reading signature file");
        }
    }

    @Override
    public void deleteProfile(String email) {

        MyProfile profile = getProfileByEmail(email);
        myProfileRepo.delete(profile);

    }
}
