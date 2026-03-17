package com.look_finder.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllUserInformationDTO {

    Long id;
    String name;
    String surname;
    String email;

    String favourite1_text_id;
    String favourite2_text_id;
    String favourite3_text_id;
    String favourite4_text_id;
    String favourite5_text_id;

    String bust;
    String waist;
    String hip;
    String sex;
}
