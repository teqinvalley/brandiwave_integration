package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.GoogleAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoogleAccountRepository extends MongoRepository<GoogleAccount, String> {

    List<GoogleAccount> findByEmail(String email);

    List<GoogleAccount> findByUser(UserModel user);
}
