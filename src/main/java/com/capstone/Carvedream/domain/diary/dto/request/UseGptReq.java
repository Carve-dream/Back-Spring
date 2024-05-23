package com.capstone.Carvedream.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "UseGptReq")
public class UseGptReq {

    @Schema(description = "꿈 내용", example = "황금돼지 꿈을 꿨어.")
    private String content;
}
