package com.capstone.Carvedream.domain.diary.dto.request;

import com.capstone.Carvedream.domain.diary.domain.Emotion;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
@Schema(description = "CreateDiaryRequest")
public class CreateDiaryReq {

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

    @Schema(description = "감정", example = "THRILL")
    private Emotion emotion;
}
