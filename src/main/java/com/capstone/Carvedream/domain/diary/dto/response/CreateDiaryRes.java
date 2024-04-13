package com.capstone.Carvedream.domain.diary.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateDiaryRes {
    private Long id;

    @Builder
    public CreateDiaryRes(Long id) {
        this.id = id;
    }
}
