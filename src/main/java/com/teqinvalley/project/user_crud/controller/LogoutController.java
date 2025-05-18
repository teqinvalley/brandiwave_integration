package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.common.CommonResponse;
import com.teqinvalley.project.user_crud.service.ITokenBlacklistService;
import com.teqinvalley.project.user_crud.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
public class LogoutController {

    @Autowired
    private ITokenBlacklistService tokenBlacklistService;

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logout(@RequestHeader("Authorization") String authHeader,
                                                 HttpServletRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
        }

        request.getSession().invalidate(); // Optional but safe
        return ResponseUtil.success("Logout successful");
    }
}