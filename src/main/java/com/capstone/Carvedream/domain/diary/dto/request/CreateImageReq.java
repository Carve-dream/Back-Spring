package com.capstone.Carvedream.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "CreateImageRequest")
public class CreateImageReq {

    @Schema(description = "내용", example = "캐나다의 밤하늘에서 오로라를 보는 몽환적인 꿈")
    private String content;
}
