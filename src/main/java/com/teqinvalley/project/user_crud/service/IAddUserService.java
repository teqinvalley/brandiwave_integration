package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.CompleteUserInfoRequestDto;
import com.teqinvalley.project.user_crud.dto.request.FirstPageSignupRequestDto;
import com.teqinvalley.project.user_crud.dto.response.SigUpResponseDto;
import com.teqinvalley.project.user_crud.model.UserModel;

import java.util.Optional;

public interface IAddUserService {

    public SigUpResponseDto addUser(FirstPageSignupRequestDto firstPageSignupRequestDto) throws Exception;

    public UserModel completeProfile(CompleteUserInfoRequestDto completeUserInfoRequestDto,String authHeader);

    Optional<UserModel> getUserByEmail(String email);
}
