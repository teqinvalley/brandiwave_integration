package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.FacebookAccount;
import com.teqinvalley.project.user_crud.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacebookAccountRepository extends MongoRepository<FacebookAccount, Long> {
    List<FacebookAccount> findByUser(UserModel user);
}

