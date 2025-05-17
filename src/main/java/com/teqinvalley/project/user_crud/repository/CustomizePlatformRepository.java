package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.CustomizePlatform;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomizePlatformRepository extends MongoRepository<CustomizePlatform, ObjectId> {
}
