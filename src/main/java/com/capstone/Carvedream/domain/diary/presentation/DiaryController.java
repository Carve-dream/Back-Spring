package com.capstone.Carvedream.domain.diary.presentation;

import com.capstone.Carvedream.domain.diary.application.DiaryService;
import com.capstone.Carvedream.domain.diary.dto.request.CreateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.request.UpdateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.request.UseGptReq;
import com.capstone.Carvedream.domain.diary.dto.response.CreateDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.FindDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.UpdateDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.UseGptRes;
import com.capstone.Carvedream.global.config.security.token.CurrentUser;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.ErrorResponse;
import com.capstone.Carvedream.global.payload.Message;
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
import org.springframework.web.bind.annotation.*;

@Tag(name = "Diary", description = "꿈 일기 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    //꿈 일기 저장
    @Operation(summary = "꿈 일기 저장", description = "오늘 기준 꿈 일기를 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = CreateDiaryRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<CommonDto> createDiary(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateDiaryReq createDiaryReq
    ) {
        return ResponseEntity.ok(diaryService.createDiary(userPrincipal, createDiaryReq));
    }

    //꿈 일기 수정
    @Operation(summary = "꿈 일기 수정", description = "꿈 일기를 수정합니다. (해몽, 이미지 제외)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateDiaryRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PatchMapping
    public ResponseEntity<CommonDto> updateDiary(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UpdateDiaryReq updateDiaryReq
            ) {
        return ResponseEntity.ok(diaryService.updateDiary(userPrincipal, updateDiaryReq));
    }

    //꿈 일기 삭제
    @Operation(summary = "꿈 일기 삭제", description = "꿈 일기를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
            @ApiResponse(responseCode = "400", description = "삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<CommonDto> deleteDiary(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "diaryId") Long diaryId
    ) {
        return ResponseEntity.ok(diaryService.deleteDiary(userPrincipal, diaryId));
    }

    //꿈 일기 조회(하나)
    @Operation(summary = "꿈 일기 조회", description = "ID에 해당하는 꿈 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FindDiaryRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/{diaryId}")
    public ResponseEntity<CommonDto> findDiary (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "diaryId") Long diaryId
    ) {
        return ResponseEntity.ok(diaryService.findDiary(userPrincipal, diaryId));
    }

    //꿈 일기 조회(모두)
    @Operation(summary = "모든 꿈 일기 조회", description = "유저에 해당하는 모든 꿈 일기를 조회합니다. (페이징은 0부터 시작)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = FindDiaryRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "조회 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping
    public ResponseEntity<CommonDto> findAllDiary (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok(diaryService.findAllDiary(userPrincipal, page));
    }

    @Operation(summary = "해몽하기", description = "문자열에 대한 해몽 결과를 보여줍니다. (작성한 일기가 없다면 id는 0으로)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해몽 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UseGptRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "해몽 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping("/interpretation")
    public ResponseEntity<CommonDto> interpret (
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody UseGptReq useGptReq
    ) {
        return ResponseEntity.ok(diaryService.interpret(userPrincipal, useGptReq));
    }

}
