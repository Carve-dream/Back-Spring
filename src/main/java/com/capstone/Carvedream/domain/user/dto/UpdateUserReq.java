package com.capstone.Carvedream.domain.user.dto;

import com.capstone.Carvedream.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "UpdateUserRequest")
public class UpdateUserReq {

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "생년월일", example = "2024-05-20")
    private LocalDate birthDate;

}
