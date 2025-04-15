package com.teqinvalley.project.user_crud.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteUserInfoRequestDto {

    private String email;

    private String fullName;

    private Long mobileNumber;

    private String password;
}
