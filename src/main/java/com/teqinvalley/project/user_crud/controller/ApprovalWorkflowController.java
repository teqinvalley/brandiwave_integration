package com.teqinvalley.project.user_crud.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.teqinvalley.project.user_crud.model.ApprovalWorkflow;
import com.teqinvalley.project.user_crud.repository.ApprovalWorkflowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/content")


public class ApprovalWorkflowController {
    private final ApprovalWorkflowRepository repository;

    public ApprovalWorkflowController(ApprovalWorkflowRepository repository) {
        this.repository = repository;
    }
    @PostMapping("/publish")
    public ResponseEntity<String> publishContent(@RequestBody ApprovalWorkflow content) {
        if (content.isApprovalRequired()) {
            if (content.getApprovers() == null || content.getApprovers().isEmpty()) {
                return ResponseEntity.badRequest().body("Approval is required. Please add approvers.");
            }
            content.setStatus("PENDING_APPROVAL");
        } else {
            content.setStatus("PUBLISHED");
        }
        repository.save(content);
        return ResponseEntity.ok("Content submitted with status: " + content.getStatus());
    }
    @GetMapping("/dashboard")
    public ResponseEntity<List<ApprovalWorkflow>> getPublishedContent() {
        return ResponseEntity.ok(repository.findByStatus("PUBLISHED"));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveContent(@PathVariable String id) {
        return repository.findById(id)
                .map(content -> {
                    if (!"PENDING_APPROVAL".equalsIgnoreCase(content.getStatus())) {
                        return ResponseEntity.badRequest().body("Content is not pending approval.");
                    }
                    content.setStatus("PUBLISHED");
                    repository.save(content);
                    return ResponseEntity.ok("Content approved and published.");
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
