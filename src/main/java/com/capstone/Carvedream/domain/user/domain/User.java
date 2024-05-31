package com.capstone.Carvedream.domain.user.domain;

import com.capstone.Carvedream.domain.common.BaseEntity;
import com.capstone.Carvedream.domain.diary.domain.Emotion;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "imageUrl")
    private Emotion imageUrl;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private String threadId;

    @Builder
    public User(Long id, String name, String email, Emotion imageUrl, String password, Role role, Gender gender, LocalDate birthDate, String threadId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        this.role = role;
        this.gender = gender;
        this.birthDate = birthDate;
        this.threadId = threadId;
    }

    public void updateUser(String name, Gender gender, LocalDate birthDate, Emotion imageUrl){
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.imageUrl = imageUrl;
    }
}
