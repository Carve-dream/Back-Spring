package com.capstone.Carvedream.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "UseGptReq")
public class UseGptReq {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "꿈 내용", example = "캡스톤 A+받는 꿈이다. 행복했다.")
    private String content;
}
