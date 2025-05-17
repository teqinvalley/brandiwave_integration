package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.MyProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyProfileRepository extends MongoRepository<MyProfile, String> {

    Optional<MyProfile> findByEmail(String email);

}
