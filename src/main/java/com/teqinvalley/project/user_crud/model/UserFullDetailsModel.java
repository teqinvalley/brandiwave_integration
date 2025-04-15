package com.teqinvalley.project.user_crud.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tbl_user_full_details_info")
public class UserFullDetailsModel {

    @Id
    @Field("full_details_id")
    private String fullDetailsId;


}
