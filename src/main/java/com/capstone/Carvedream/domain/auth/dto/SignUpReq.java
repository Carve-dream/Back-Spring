package com.capstone.Carvedream.domain.auth.dto;

import com.capstone.Carvedream.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignUpReq {

    @Schema( type = "string", example = "string", description="계정 명 입니다.")
    @NotBlank
    private String name;

    @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    @NotBlank
    @Email
    private String email;

    @Schema( type = "string", example = "string", description="계정 비밀번호 입니다.")
    @NotBlank
    private String password;

    @Schema(description = "성별", example = "MALE")
    private Gender gender;

    @Schema(description = "생년월일", example = "2001-01-01")
    private LocalDate birthDate;
}
