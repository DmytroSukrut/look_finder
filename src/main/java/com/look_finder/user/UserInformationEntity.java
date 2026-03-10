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

    @Column(name = "favourite", nullable = false, columnDefinition = "vector(384)", insertable = false, updatable = false)
    private String favourite;

    @Column(name = "bust", nullable = false)
    private String bust;

    @Column(name = "waist", nullable = false)
    private String waist;

    @Column(name = "hip", nullable = false)
    private String hip;

    @Column(name = "sex", nullable = false)
    private String sex;
}
