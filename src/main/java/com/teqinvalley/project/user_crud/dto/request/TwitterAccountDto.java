package com.teqinvalley.project.user_crud.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwitterAccountDto {

    private String twitterId;

    private String name;
}
