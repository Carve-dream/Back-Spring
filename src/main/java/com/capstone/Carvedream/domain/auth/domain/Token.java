package com.capstone.Carvedream.domain.auth.domain;

import com.capstone.Carvedream.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name="token")
@Entity
public class Token extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", length = 1024 , nullable = false)
    private String userEmail;

    @Column(name = "refresh_token", length = 1024 , nullable = false)
    private String refreshToken;

    public Token(){}

    public Token updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Builder
    public Token(String userEmail, String refreshToken) {
        this.userEmail = userEmail;
        this.refreshToken = refreshToken;
    }

}
