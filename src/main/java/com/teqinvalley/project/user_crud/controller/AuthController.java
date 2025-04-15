package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.common.CommonResponse;
import com.teqinvalley.project.user_crud.common.SuccessResponse;
import com.teqinvalley.project.user_crud.dto.request.CompleteUserInfoRequestDto;
import com.teqinvalley.project.user_crud.dto.request.FirstPageSignupRequestDto;
import com.teqinvalley.project.user_crud.dto.request.LoginRequestDto;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.IAddUserService;
import com.teqinvalley.project.user_crud.utils.ResponseUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
     IAddUserService iAddUserService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> registerUser( @RequestBody FirstPageSignupRequestDto firstPageSignupRequestDto) throws Exception {
        return ResponseUtil.success(iAddUserService.addUser(firstPageSignupRequestDto));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<CommonResponse> completeProfile(
            @RequestBody CompleteUserInfoRequestDto completeUserInfoRequestDto,
            @RequestHeader("Authorization") String authHeader) throws Exception {

        return ResponseUtil.success(
                iAddUserService.completeProfile(completeUserInfoRequestDto, authHeader)
        );
    }

    @GetMapping("/findByEmail/{email}")
    public ResponseEntity<CommonResponse> completeProfile(@PathVariable String email) {
        Optional<UserModel> user = iAddUserService.getUserByEmail(email);
        return ResponseUtil.success(user);
    }




//    @PostMapping("/login")
//    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDto loginRequest) throws Exception {
//        UserModel user = userRepository.findByEmail(loginRequest.getEmail())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String decrypted = AESUtil.decrypt(user.getPassword());
//
//        if (!decrypted.equals(loginRequest.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//
//        String token = JwtUtil.generateToken(user.getEmail());
//        return ResponseEntity.ok(Map.of("token", token));
//    }
}

