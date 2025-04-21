package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.TwitterAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TwitterAccountRepository extends MongoRepository<TwitterAccount, String> {
    List<TwitterAccount> findByUser(UserModel user);
}
