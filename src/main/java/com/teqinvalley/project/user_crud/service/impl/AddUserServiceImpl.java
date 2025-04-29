package com.teqinvalley.project.user_crud.service.impl;

import com.teqinvalley.project.user_crud.dto.request.CompleteUserInfoRequestDto;
import com.teqinvalley.project.user_crud.dto.request.FirstPageSignupRequestDto;
import com.teqinvalley.project.user_crud.dto.response.SigUpResponseDto;
import com.teqinvalley.project.user_crud.exception.UserAlreadyExistException;
import com.teqinvalley.project.user_crud.model.UserModel;
import com.teqinvalley.project.user_crud.repository.UserRepository;
import com.teqinvalley.project.user_crud.service.IAddUserService;
import com.teqinvalley.project.user_crud.utils.AESUtil;
import com.teqinvalley.project.user_crud.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AddUserServiceImpl implements IAddUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtil jwtUtil;


    @Override
    public SigUpResponseDto addUser(FirstPageSignupRequestDto firstPageSignupRequestDto) {
        if (userRepository.findByEmail(firstPageSignupRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exists");
        }

        UserModel userModel = UserModel.builder()
                .email(firstPageSignupRequestDto.getEmail())
                .organisationName(firstPageSignupRequestDto.getOrganisationName())
                .completedProfile(false)
                .build();

        userRepository.save(userModel);
        String token = jwtUtil.generateToken(firstPageSignupRequestDto.getEmail());

        // Return token + user info
        return new SigUpResponseDto(firstPageSignupRequestDto.getEmail(), token);
    }

    @Override
    public UserModel completeProfile(CompleteUserInfoRequestDto completeUserInfoRequestDto, String authHeader) {
        Optional<UserModel> optionalUser = userRepository.findByEmail(completeUserInfoRequestDto.getEmail());
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");

        // ✅ Validate the token
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        // ✅ Extract email from token
        String tokenEmail = jwtUtil.extractUsername(token);

        // ✅ Compare with email in request for extra safety
        if (!tokenEmail.equals(completeUserInfoRequestDto.getEmail())) {
            throw new RuntimeException("Token email does not match request email");
        }
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with email: " + completeUserInfoRequestDto.getEmail());
        }

        UserModel user = optionalUser.get();

        user.setFullName(completeUserInfoRequestDto.getFullName());
        try {
            user.setPassword(AESUtil.encrypt(completeUserInfoRequestDto.getPassword()));
        } catch (Exception e) {
            throw new UserAlreadyExistException("Failed to encrypt password");
        }
        user.setMobileNumber(completeUserInfoRequestDto.getMobileNumber());
        user.setCompletedProfile(true);

        userRepository.save(user);

        return user;
    }

    @Override
    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
