package com.capstone.Carvedream.domain.diary.domain;

import com.capstone.Carvedream.domain.common.BaseEntity;
import com.capstone.Carvedream.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "diary")
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "image_url")
    private String image_url;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "interpretation")
    private String interpretation;

    @Column(name = "emotion")
    @Enumerated(EnumType.STRING)
    private Emotion emotion;

    @Column(name = "start_sleep")
    private LocalTime start_sleep;

    @Column(name = "end_sleep")
    private LocalTime end_sleep;

    @Column(name = "changed")
    private Boolean changed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Diary(String title, String content, String image_url, LocalDate date, String interpretation, Emotion emotion, LocalTime start_sleep, LocalTime end_sleep, User user) {
        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.date = date;
        this.interpretation = interpretation;
        this.emotion = emotion;
        this.start_sleep = start_sleep;
        this.end_sleep = end_sleep;
        this.changed = false;
        this.user = user;
    }

    public void updateDiary(String title, String content, LocalDate date, Emotion emotion, LocalTime start_sleep, LocalTime end_sleep) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.start_sleep = start_sleep;
        this.end_sleep = end_sleep;
        this.changed = true;
    }

    public void updateInterpretation(String interpretation) {
        this.interpretation = interpretation;
    }

    public void updateImageUrl(String image_url) {
        this.image_url = image_url;
    }
}
