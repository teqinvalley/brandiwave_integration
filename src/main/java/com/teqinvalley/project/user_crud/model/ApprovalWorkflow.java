package com.teqinvalley.project.user_crud.model;
import org.springframework.data.annotation.Id;

import java.util.List;


import java.util.List;

public class ApprovalWorkflow
{
    @Id
private String id;
private String title;
private String body;
private boolean approvalRequired;
private List<String> approvers;
private String status; // PUBLISHED or PENDING_APPROVAL

// Getters and Setters
public String getId() { return id; }
public void setId(String id) { this.id = id; }

public String getTitle() { return title; }
public void setTitle(String title) { this.title = title; }

public String getBody() { return body; }
public void setBody(String body) { this.body = body; }

public boolean isApprovalRequired() { return approvalRequired; }
public void setApprovalRequired(boolean approvalRequired) { this.approvalRequired = approvalRequired; }

public List<String> getApprovers() { return approvers; }
public void setApprovers(List<String> approvers) { this.approvers = approvers; }

public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }
}


