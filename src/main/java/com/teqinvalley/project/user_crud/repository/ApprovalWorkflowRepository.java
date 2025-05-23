package com.teqinvalley.project.user_crud.repository;

import com.teqinvalley.project.user_crud.model.ApprovalWorkflow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ApprovalWorkflowRepository  extends MongoRepository<ApprovalWorkflow, String> {
    // List<ApprovalWorkflow> findByStatus(String status);

    List<ApprovalWorkflow> findByStatus(String status);
}

