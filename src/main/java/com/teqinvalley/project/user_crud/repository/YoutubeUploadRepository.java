package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.YoutubeVideoUpload;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeUploadRepository extends MongoRepository<YoutubeVideoUpload,String> {

}
