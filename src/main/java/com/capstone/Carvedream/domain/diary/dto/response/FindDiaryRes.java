package com.capstone.Carvedream.domain.diary.dto.response;

import com.capstone.Carvedream.domain.diary.domain.Emotion;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Data
public class FindDiaryRes {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "좋은 꿈")
    private String title;

    @Schema(description = "내용", example = "캡스톤 A+받는 꿈이다. 행복했다.")
    private String content;

    @Schema(description = "이미지 사진", example = "https://i.namu.wiki/i/GQMqb8jtiqpCo6_US7jmWDO30KfPB2MMvbdURVub61Rs6ALKqbG-nUATj-wNk7bXXWIDjiLHJxWYkTELUgybkA.webp")
    private String image_url;

    @Schema(description = "수면 시작 시간", example = "00:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime start_sleep;

    @Schema(description = "수면 종료 시간", example = "00:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime end_sleep;

    @Schema(description = "날짜", example = "2024-04-13")
    private LocalDate date;

    @Schema(description = "감정", example = "THRILL")
    private Emotion emotion;

    @Schema(description = "해몽", example = "이 꿈은 탐험적인 성격을 가진 일상 생활의 변화를 예상할 수 있습니다.")
    private String interpretation;

    @CollectionTable(name = "diary_tags", joinColumns = @JoinColumn(name = "diary_id"))
    @Schema(description = "태그", example = "[\"태그1\", \"태그2\"]")
    private Set<String> tags;

    @Builder
    public FindDiaryRes(Long id, String title, String content, String image_url, LocalTime start_sleep, LocalTime end_sleep, LocalDate date, Emotion emotion, String interpretation, Set<String> tags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image_url = image_url;
        this.start_sleep = start_sleep;
        this.end_sleep = end_sleep;
        this.date = date;
        this.emotion = emotion;
        this.tags = tags;
        this.interpretation = interpretation;
    }
}
