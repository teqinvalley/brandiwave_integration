package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.dto.request.UserEngagementRequestDto;
import com.teqinvalley.project.user_crud.service.IFacebookAccountService;
import com.teqinvalley.project.user_crud.service.IGoogleAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/dashboard")
public class UserDashboardController {

    @Autowired
    private IGoogleAccountService googleAccountService;
    @Autowired
    private IFacebookAccountService facebookAccountService;

    @PostMapping("/engagement")
    public Map<String, Object> fetchYoutubeEngagementDetails(@RequestBody UserEngagementRequestDto userEngagementRequestDto) throws  Exception{
       Map<String,Object> response=new HashMap<>();
        response=googleAccountService.getYoutubeEngagement(userEngagementRequestDto);
        response.putAll(facebookAccountService.getFacebookEngagement(userEngagementRequestDto));

        return response;
    }



}
