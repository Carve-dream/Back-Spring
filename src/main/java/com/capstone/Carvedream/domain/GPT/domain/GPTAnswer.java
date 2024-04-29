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
@Table(name = "gpt_answer")
public class GPTAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(name = "gpt_question_id")
    private GPTQuestion question;

    @Builder
    public GPTAnswer(Long id, String content, GPTQuestion question) {
        this.id = id;
        this.content = content;
        this.question = question;
    }
}
