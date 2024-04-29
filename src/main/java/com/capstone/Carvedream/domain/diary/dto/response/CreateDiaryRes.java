package com.capstone.Carvedream.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateDiaryRes {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Builder
    public CreateDiaryRes(Long id) {
        this.id = id;
    }
}
