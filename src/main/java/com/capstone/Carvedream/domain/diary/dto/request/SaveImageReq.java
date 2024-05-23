package com.capstone.Carvedream.domain.diary.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "SaveImageRequest")
public class SaveImageReq {

    @Schema(description = "일기ID", example = "1")
    private Long id;

    @Schema(description = "이미지url", example = "https://carve-dream.s3.ap-northeast-2.amazonaws.com/dream/1716471329750.png")
    private String url;
}
