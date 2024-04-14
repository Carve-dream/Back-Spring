package com.capstone.Carvedream.domain.diary.dto.response;

import lombok.Data;

@Data
public class UseGptRes {

    private String answer;

    public UseGptRes(String answer) {
        this.answer = answer;
    }
}
