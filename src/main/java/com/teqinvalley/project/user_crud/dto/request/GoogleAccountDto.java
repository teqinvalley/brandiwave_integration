package com.teqinvalley.project.user_crud.dto.request;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAccountDto {

    private String googleId;
    private String name;
}
