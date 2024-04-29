package com.capstone.Carvedream.domain.GPT.domain;

import com.capstone.Carvedream.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "gpt_question")
public class GPTQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Builder
    public GPTQuestion(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}