package com.look_finder.position;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "current_available_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text_id", nullable = false)
    private String text_id;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "sex", nullable = false)
    private String sex;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "positions_color", nullable = false)
    private String positionsColor;

    @Column(name = "size", nullable = false)
    private String size;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "display", nullable = false)
    private String display;

    @Column(name = "photo_0", nullable = false)
    private String photo0;

    @Column(name = "photo_1", nullable = false)
    private String photo1;

    @Column(name = "photo_2", nullable = false)
    private String photo2;

    @Column(name = "photo_3", nullable = false)
    private String photo3;

    @Column(name = "embedding", columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String embedding;

    @Column(name = "embedding_text", insertable = false, updatable = false)
    private String embeddingText;
}
