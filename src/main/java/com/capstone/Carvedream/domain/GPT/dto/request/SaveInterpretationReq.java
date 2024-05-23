package com.capstone.Carvedream.domain.GPT.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "SaveInterpretationRequest")
public class SaveInterpretationReq {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "해몽 내용", example = "황금돼지 꿈은 풍요와 행운을 상징합니다. 이 꿈은 당신이 부를 누리고 행복한 삶을 살고 있다는 것을 나타낼 수 있습니다.")
    private String content;
}
