package com.look_finder.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    String name;

    @NotBlank
    String surname;

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 8)
    String password;
}
