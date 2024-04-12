package com.capstone.Carvedream.domain.diary.presentation;

import com.capstone.Carvedream.domain.diary.application.DiaryService;
import com.capstone.Carvedream.domain.diary.dto.request.CreateDiaryReq;
import com.capstone.Carvedream.global.config.security.token.CurrentUser;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Diary", description = "꿈 일기 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    //꿈 일기 저장
    @Operation(summary = "꿈 일기 저장", description = "오늘 기준 꿈 일기를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateDiaryReq.class) ) } ),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<CommonDto> createDiary(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDiaryReq createDiaryReq
    ) throws JsonProcessingException {
        return ResponseEntity.ok(diaryService.createDiary(userPrincipal, createDiaryReq));
    }

}
