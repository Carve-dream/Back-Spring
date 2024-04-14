package com.capstone.Carvedream.domain.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindFortuneRes {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "날짜", example = "2024-04-13")
    private LocalDateTime createAt;

    @Schema(description = "내용", example = "일찍 잠에 들어보는건 어때요?")
    private String content;

    @Builder
    public FindFortuneRes(Long id, LocalDateTime createAt, String content) {
        this.id = id;
        this.createAt = createAt;
        this.content = content;
    }
}
