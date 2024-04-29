package com.capstone.Carvedream.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UseGptRes {

    @Schema(description = "해몽결과", example = "돼지꿈은 돈을 상징합니다.")
    private String answer;

    public UseGptRes(String answer) {
        this.answer = answer;
    }
}
