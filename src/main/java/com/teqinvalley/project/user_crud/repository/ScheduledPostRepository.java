package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.ScheduledPosts;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledPostRepository extends MongoRepository<ScheduledPosts, String> {

    List<ScheduledPosts> findByPostedFalseAndScheduledTimeBefore(LocalDateTime time);
}
