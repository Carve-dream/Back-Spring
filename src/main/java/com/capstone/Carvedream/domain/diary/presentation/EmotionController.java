package com.capstone.Carvedream.domain.diary.presentation;

import com.capstone.Carvedream.domain.diary.application.DiaryService;
import com.capstone.Carvedream.global.config.security.token.CurrentUser;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Emotion", description = "감정 지도 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/emotion")

public class EmotionController {

    private final DiaryService diaryService;

    //감정 이모티콘 불러오기
    @GetMapping
    public ResponseEntity<CommonDto> getEmotionCal(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month
    ) {

        return ResponseEntity.ok(diaryService.getEmotionCalendar(userPrincipal, year, month));

    }
}
