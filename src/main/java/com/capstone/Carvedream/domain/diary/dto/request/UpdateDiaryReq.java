package com.capstone.Carvedream.domain.diary.dto.request;

import com.capstone.Carvedream.domain.diary.domain.Emotion;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Schema(description = "UpdateDiaryRequest")
public class UpdateDiaryReq {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "제목", example = "좋은 꿈")
    private String title;

    @Schema(description = "내용", example = "캡스톤 A+받는 꿈이다. 행복했다.")
    private String content;

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
}
