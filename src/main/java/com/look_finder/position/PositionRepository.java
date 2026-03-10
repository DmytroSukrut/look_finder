package com.look_finder.position;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    List<PositionEntity> findByCategoryAndSexAndSize(String category, String sex, String size);

    @Modifying
    @Transactional
    @Query(value = """
        update current_available_products
        set embedding = cast(:queryEmbedding as vector),
        embedding_text = :embeddingText
        where id = :id 
    """, nativeQuery = true)
    void updateEmbedding(@Param("id") long id,
                         @Param("queryEmbedding") String queryEmbeddingLiteral,
                         @Param("embeddingText") String embeddingText);

    @Query(value = """
        select *
        from current_available_products
        where embedding is not null
            and size = :size
            and sex = :sex
        order by embedding <=> cast(:queryEmbedding as vector)
    """, nativeQuery = true)
    List<PositionEntity> findSimilarProducts(@Param("queryEmbedding") String queryEmbedding,
                                             @Param("size") String size,
                                             @Param("sex") String sex);
}
