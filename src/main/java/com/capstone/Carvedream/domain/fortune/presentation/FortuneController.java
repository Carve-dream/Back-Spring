package com.capstone.Carvedream.domain.fortune.presentation;

import com.capstone.Carvedream.domain.diary.dto.response.UseGptRes;
import com.capstone.Carvedream.domain.fortune.application.FortuneService;
import com.capstone.Carvedream.global.config.security.token.CurrentUser;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fortune", description = "포춘쿠키 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    @Operation(summary = "포춘쿠키 생성", description = "나의 데이터를 통해 포춘쿠키 하나를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UseGptRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<CommonDto> createFortune (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(fortuneService.createFortune(userPrincipal));
    }
}
