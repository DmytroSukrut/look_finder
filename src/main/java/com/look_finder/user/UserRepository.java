package com.look_finder.user;


import com.look_finder.DTO.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserDTO findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT u.password
        FROM UserEntity u
            WHERE u.email = :email
    """)
    Optional<String> findPasswordByEmail(@Param("email") String email);

    @Query("""
      SELECT COALESCE(
        (SELECT MIN(u.id) + 1
         FROM UserEntity u
         WHERE NOT EXISTS (
           SELECT 1 FROM UserEntity u2 WHERE u2.id = u.id + 1
         )
        ),
        1
      )
    """)
    Long findNextFreeId();
}
