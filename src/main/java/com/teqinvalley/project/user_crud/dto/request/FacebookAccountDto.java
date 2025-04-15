package com.teqinvalley.project.user_crud.dto.request;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FacebookAccountDto {

    private String facebookId;
    private String name;
}
