package com.look_finder.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInformationRepository extends JpaRepository<UserInformationEntity, Long> {

    UserInformationEntity findByUserId(long id);

    @Modifying
    @Transactional
    @Query(value = """
        update user_information
        set favourite = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite(@Param("id") long id,
                         @Param("favouriteEmbedding") String queryEmbeddingLiteral);

}
