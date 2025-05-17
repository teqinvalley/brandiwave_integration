package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.enumeration.MyProfileDesignation;
import com.teqinvalley.project.user_crud.enumeration.MyProfileRole;
import com.teqinvalley.project.user_crud.model.MyProfile;
import com.teqinvalley.project.user_crud.service.MyProfileService;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

@RestController
@RequestMapping("/myprofile")
public class MyProfileController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private MyProfileService service;

    private String extractEmailFromToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return jwtUtil.extractUsername(header.substring(7));

        }
        throw new RuntimeException("Invalid Token");
    }
    @GetMapping("/Complete-Profile")
    public ResponseEntity<MyProfile> getProfile(HttpServletRequest request){
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(service.getProfileByEmail(email));
    }
    @PutMapping("/update")
    public ResponseEntity<MyProfile> updateProfile(@RequestBody MyProfile profile, HttpServletRequest request){
        String email = extractEmailFromToken(request);
        return ResponseEntity.ok(service.updateProfile(email, profile));
    }
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("image")MultipartFile image, HttpServletRequest request) throws IOException {

        String email = extractEmailFromToken(request);
        service.uploadProfilePicture(email, image);
        return ResponseEntity.ok("Image uploaded successfully");

    }
    @PostMapping("/upload-signature")
    public ResponseEntity<String> uploadSignature(@RequestParam("Signature") MultipartFile signature, HttpServletRequest request){
        String email = extractEmailFromToken(request);
        service.uploadSignature(email, signature);
        return  ResponseEntity.ok("Signature Uploaded successfully");

    }
    @DeleteMapping("/delete-profile")
    public ResponseEntity<String> deleteAccount(HttpServletRequest request){

        String email = extractEmailFromToken(request);
        service.deleteProfile(email);
        return ResponseEntity.ok("Profile Deleted successfully");

    }
    @GetMapping("/roles")
    public ResponseEntity<MyProfileRole[]> getRoles() {
        return ResponseEntity.ok(MyProfileRole.values());
    }

    @GetMapping("/designations")
    public ResponseEntity<MyProfileDesignation[]> getDesignations() {
        return ResponseEntity.ok(MyProfileDesignation.values());
    }
}

