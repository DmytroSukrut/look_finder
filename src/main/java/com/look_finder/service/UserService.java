package com.look_finder.service;

import com.look_finder.DTO.UserDTO;
import com.look_finder.errors.AppException;
import com.look_finder.errors.EmailAlreadyExistsException;
import com.look_finder.errors.UserIsntRegistratedException;
import com.look_finder.user.UserEntity;
import com.look_finder.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public UserDTO saveUser(
            String name,
            String surname,
            String email,
            String password
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity user = new UserEntity();
        user.setId(userRepository.findNextFreeId());
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        user.setPassword(passwordService.encodePassword(password));

        userRepository.save(user);

        return userRepository.findByEmail(email);
    }

    public UserDTO loginUser(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserIsntRegistratedException();
        }

        String encodedPassword = userRepository.findPasswordByEmail(email)
                .orElseThrow(() -> new UserIsntRegistratedException());

        if (passwordService.comparePasswords(password, encodedPassword)) {
            return userRepository.findByEmail(email);
        } else {
            throw new AppException("PASSWORD_OR_EMAIL_ERROR", "Wrong password or email adres");
        }
    }
}
