package com.look_finder.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    List<PositionEntity> findByCategoryAndSexAndSize(String category, String sex, String size);

}
