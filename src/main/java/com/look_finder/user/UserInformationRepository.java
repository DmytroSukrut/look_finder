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
        set favourite1 = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite1(@Param("id") long id,
                         @Param("favouriteEmbedding") String queryEmbeddingLiteral);

    @Modifying
    @Transactional
    @Query(value = """
        update user_information
        set favourite2 = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite2(@Param("id") long id,
                          @Param("favouriteEmbedding") String queryEmbeddingLiteral);

    @Modifying
    @Transactional
    @Query(value = """
        update user_information
        set favourite3 = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite3(@Param("id") long id,
                          @Param("favouriteEmbedding") String queryEmbeddingLiteral);

    @Modifying
    @Transactional
    @Query(value = """
        update user_information
        set favourite4 = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite4(@Param("id") long id,
                          @Param("favouriteEmbedding") String queryEmbeddingLiteral);

    @Modifying
    @Transactional
    @Query(value = """
        update user_information
        set favourite5 = cast(:favouriteEmbedding as vector)
        where user_id = :id 
    """, nativeQuery = true)
    void updateFavourite5(@Param("id") long id,
                          @Param("favouriteEmbedding") String queryEmbeddingLiteral);
}
