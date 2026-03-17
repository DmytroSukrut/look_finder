package com.look_finder.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user_information")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserInformationEntity {

    @Id
    @Column(name = "user_id", insertable = true)
    private Long userId;

    @Column(name = "favourite1", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite1;

    @Column(name = "favourite1_text_id")
    private String favourite1TextId;

    @Column(name = "favourite2", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite2;

    @Column(name = "favourite2_text_id")
    private String favourite2TextId;

    @Column(name = "favourite3", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite3;

    @Column(name = "favourite3_text_id")
    private String favourite3TextId;

    @Column(name = "favourite4", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite4;

    @Column(name = "favourite4_text_id")
    private String favourite4TextId;

    @Column(name = "favourite5", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite5;

    @Column(name = "favourite5_text_id")
    private String favourite5TextId;

    @Column(name = "bust", nullable = false)
    private String bust;

    @Column(name = "waist", nullable = false)
    private String waist;

    @Column(name = "hip", nullable = false)
    private String hip;

    @Column(name = "sex", nullable = false)
    private String sex;
}
