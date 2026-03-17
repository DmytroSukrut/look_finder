package com.look_finder.service;

import com.look_finder.DTO.AllUserInformationDTO;
import com.look_finder.DTO.UserDTO;
import com.look_finder.errors.AppException;
import com.look_finder.errors.EmailAlreadyExistsException;
import com.look_finder.errors.UserIsntRegistratedException;
import com.look_finder.position.PositionEntity;
import com.look_finder.position.PositionRepository;
import com.look_finder.user.UserEntity;
import com.look_finder.user.UserInformationEntity;
import com.look_finder.user.UserInformationRepository;
import com.look_finder.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInformationRepository userInformationRepository;
    private final PasswordService passwordService;
    private final PositionRepository positionRepository;

    public AllUserInformationDTO saveUser(
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


        UserInformationEntity information = new UserInformationEntity();
        information.setUserId(user.getId());
        information.setBust("0");
        information.setWaist("0");
        information.setHip("0");
        information.setSex("f");

        PositionEntity position = positionRepository.findRandom("36", "XS", "f");

        information.setFavourite1TextId(position.getText_id());

        userInformationRepository.save(information);

        userInformationRepository.updateFavourite1(user.getId(), position.getEmbedding());

        UserDTO userDTO = userRepository.findByEmail(email);
        UserInformationEntity userInformationEntity = userInformationRepository.findByUserId(userDTO.getId());

        return new AllUserInformationDTO(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getEmail(),
                userInformationEntity.getFavourite1TextId(),
                userInformationEntity.getFavourite2TextId(),
                userInformationEntity.getFavourite3TextId(),
                userInformationEntity.getFavourite4TextId(),
                userInformationEntity.getFavourite5TextId(),
                userInformationEntity.getBust(),
                userInformationEntity.getWaist(),
                userInformationEntity.getHip(),
                userInformationEntity.getSex()
                );
    }

    public AllUserInformationDTO loginUser(String email, String password) {
        if (!userRepository.existsByEmail(email)) {
            throw new UserIsntRegistratedException();
        }

        String encodedPassword = userRepository.findPasswordByEmail(email)
                .orElseThrow(() -> new UserIsntRegistratedException());

        if (passwordService.comparePasswords(password, encodedPassword)) {
            UserDTO userDTO = userRepository.findByEmail(email);
            UserInformationEntity userInformationEntity = userInformationRepository.findByUserId(userDTO.getId());

            return new AllUserInformationDTO(
                    userDTO.getId(),
                    userDTO.getName(),
                    userDTO.getSurname(),
                    userDTO.getEmail(),
                    userInformationEntity.getFavourite1TextId(),
                    userInformationEntity.getFavourite2TextId(),
                    userInformationEntity.getFavourite3TextId(),
                    userInformationEntity.getFavourite4TextId(),
                    userInformationEntity.getFavourite5TextId(),
                    userInformationEntity.getBust(),
                    userInformationEntity.getWaist(),
                    userInformationEntity.getHip(),
                    userInformationEntity.getSex()
            );
        } else {
            throw new AppException("PASSWORD_OR_EMAIL_ERROR", "Wrong password or email adres");
        }
    }
}
