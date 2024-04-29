package com.capstone.Carvedream.domain.diary.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateDiaryRes {

    @Schema(description = "변경체크", example = "true")
    private Boolean changed;

    @Schema(description = "안내메시지", example = "꿈이 성공적으로 수정되었습니다.")
    private String message;

    @Builder
    public UpdateDiaryRes(Boolean changed, String message) {
        this.changed = changed;
        this.message = message;
    }
}
