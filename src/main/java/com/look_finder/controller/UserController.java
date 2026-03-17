package com.look_finder.controller;

import com.look_finder.DTO.AllUserInformationDTO;
import com.look_finder.DTO.LoginDTO;
import com.look_finder.DTO.RegisterDTO;
import com.look_finder.DTO.UserDTO;
import com.look_finder.service.UserService;
import com.look_finder.user.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public AllUserInformationDTO registerUser(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        return userService.saveUser(
                registerDTO.getName(),
                registerDTO.getSurname(),
                registerDTO.getEmail(),
                registerDTO.getPassword()
        );
    }

    @PostMapping("/login")
    public AllUserInformationDTO login(
            @RequestBody @Valid LoginDTO loginDTO
    ){
       return userService.loginUser(
               loginDTO.getEmail(),
               loginDTO.getPassword()
       );
    }
}
