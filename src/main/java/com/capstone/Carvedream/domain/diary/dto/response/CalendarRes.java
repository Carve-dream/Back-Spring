package com.capstone.Carvedream.domain.diary.dto.response;

import com.capstone.Carvedream.domain.diary.domain.Emotion;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CalendarRes {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "날짜", example = "2024-04-15")
    private LocalDate date;

    @Schema(description = "감정", example = "JOY")
    private Emotion emotion;

    @Builder
    public CalendarRes(Long id, LocalDate date, Emotion emotion) {
        this.id = id;
        this.date = date;
        this.emotion = emotion;
    }
}
