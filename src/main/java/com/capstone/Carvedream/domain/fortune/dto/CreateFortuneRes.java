package com.capstone.Carvedream.domain.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateFortuneRes {

    @Schema(description = "포춘쿠키 내용", example = "오늘 하루에 최선을 다해보세요.")
    private String answer;

    @Builder
    public CreateFortuneRes(String answer) {
        this.answer = answer;
    }
}