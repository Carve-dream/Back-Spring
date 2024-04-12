package com.capstone.Carvedream.domain.diary.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateDiaryRes {

    private Boolean changed;

    private String message;

    @Builder
    public UpdateDiaryRes(Boolean changed, String message) {
        this.changed = changed;
        this.message = message;
    }
}
