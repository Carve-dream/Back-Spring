package com.capstone.Carvedream.domain.GPT.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class ChatRes {

    @Schema(description = "응답", example = "꿈에서 돼지를 꿔보다니 특이한 꿈이었겠네요. 돼지는 풍요와 행운을 상징하는 동물이기도 하니 좋은 일이 생길 수도 있을 거예요.")
    private String answer;

    @Builder
    public ChatRes(String answer) {
        this.answer = answer;
    }
}
