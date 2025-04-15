package com.teqinvalley.project.user_crud.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SigUpResponseDto {

    private String email;
    private String token;
}
