package com.capstone.Carvedream.domain.fortune.presentation;

import com.capstone.Carvedream.domain.fortune.application.FortuneService;
import com.capstone.Carvedream.domain.fortune.dto.CreateFortuneRes;
import com.capstone.Carvedream.domain.fortune.dto.FindFortuneRes;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Fortune", description = "포춘쿠키 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fortune")
public class FortuneController {

    private final FortuneService fortuneService;

    @Operation(summary = "포춘쿠키 생성", description = "나의 데이터를 통해 포춘쿠키 하나를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateFortuneRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "생성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<CommonDto> createFortune (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(fortuneService.createFortune(userPrincipal));
    }

    //포춘쿠키 조회(모두)
    @Operation(summary = "월별 포춘쿠키 조회", description = "월에 해당하는 유저의 포춘쿠키를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FindFortuneRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping
    public ResponseEntity<CommonDto> findFortuneByMonth (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "year") Integer year,
            @RequestParam(name = "month") Integer month
    ) {
        return ResponseEntity.ok(fortuneService.findFortune(userPrincipal, year, month));
    }
}
