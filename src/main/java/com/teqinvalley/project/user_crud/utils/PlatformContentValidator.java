package com.teqinvalley.project.user_crud.utils;

import com.teqinvalley.project.user_crud.dto.request.PlatformContentDto;

public class PlatformContentValidator {

    public static void  validatePlatform(PlatformContentDto Dto){
        String platform = Dto.getPlatform().toUpperCase();

        switch (platform){
            case "YOUTUBE":
                if(Dto.getImageUrl()!=null){
                    throw  new IllegalArgumentException("YouTube does not support images");
                }
                if(Dto.getVideoUrl() == null){
                    throw new IllegalArgumentException("Youtube requires a video");
                }
                break;
            case "FACEBOOK":
            case "INSTAGRAM":
                if(Dto.getImageUrl() == null && Dto.getVideoUrl() == null){
                    throw new IllegalArgumentException(platform + "requires an image or video");
                }
                break;
        }
    }
}
