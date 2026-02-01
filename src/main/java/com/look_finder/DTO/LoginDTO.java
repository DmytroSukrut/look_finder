package com.look_finder.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @Email
    @NotBlank
    String email;

    @NotBlank
    String password;

}
