package com.capstone.Carvedream.domain.GPT.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "ChatRequest")
public class ChatReq {

    @Schema(description = "질문", example = "나 돼지꿈 꿨어.")
    private String question;
}
