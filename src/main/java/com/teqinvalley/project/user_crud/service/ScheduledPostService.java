package com.teqinvalley.project.user_crud.service;

import com.teqinvalley.project.user_crud.dto.request.ScheduleRequestDto;
import com.teqinvalley.project.user_crud.model.ScheduledPosts;

public interface ScheduledPostService {

    ScheduledPosts schedule(String platform, ScheduleRequestDto request);
    void checkAndPost(); // Called by scheduler
}
