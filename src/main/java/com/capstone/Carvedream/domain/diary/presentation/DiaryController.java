package com.capstone.Carvedream.domain.diary.presentation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Diary", description = "Diary API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {



}
