package com.teqinvalley.project.user_crud.controller;

import com.teqinvalley.project.user_crud.common.SuccessResponse;
import com.teqinvalley.project.user_crud.dto.request.ScheduleRequestDto;
import com.teqinvalley.project.user_crud.model.ScheduledPosts;
import com.teqinvalley.project.user_crud.service.ScheduledPostService;
import com.teqinvalley.project.user_crud.service.impl.ScheduledPostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class ScheduledPostController {

    @Autowired
    private ScheduledPostService postService;

//    @PostMapping("/{platform}")
//    public ResponseEntity<ScheduledPosts> schedulePost(@PathVariable String platform, @RequestBody ScheduleRequestDto request){
//
//        ScheduledPosts schedule = postService.schedule(platform.toLowerCase(), request);
//
//        return ResponseEntity.ok(schedule);
//    }
@PostMapping("/facebook")
public ResponseEntity<ScheduledPosts> scheduleFacebook(@RequestBody ScheduleRequestDto request,  @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    return ResponseEntity.ok(postService.schedule("facebook", request, token));
}

    @PostMapping("/instagram")
    public ResponseEntity<ScheduledPosts> scheduleInstagram(@RequestBody ScheduleRequestDto request, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(postService.schedule("instagram", request, token  ));
    }

    @PostMapping("/youTube")
    public ResponseEntity<ScheduledPosts> scheduleYoutube(@RequestBody ScheduleRequestDto request, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(postService.schedule("youTube", request, token));
    }

}
