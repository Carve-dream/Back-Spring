package com.capstone.Carvedream.domain.diary.application;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.diary.domain.repository.DiaryRepository;
import com.capstone.Carvedream.domain.diary.dto.request.CreateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.request.UpdateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.response.CreateDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.UpdateDiaryRes;
import com.capstone.Carvedream.domain.diary.exception.InvalidDiaryException;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    // 꿈 일기 생성
    @Transactional
    public CommonDto createDiary(UserPrincipal userPrincipal, CreateDiaryReq createDiaryReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        Diary diary = Diary.builder()
                .title(createDiaryReq.getTitle())
                .content(createDiaryReq.getContent())
                .start_sleep(createDiaryReq.getStart_sleep())
                .end_sleep(createDiaryReq.getEnd_sleep())
                .emotion(createDiaryReq.getEmotion())
                .date(LocalDate.now())
                .user(user)
                .build();

        diaryRepository.save(diary);

        CreateDiaryRes createDiaryRes = new CreateDiaryRes(diary.getId());

        return new CommonDto(true, createDiaryRes);
    }

    // 꿈 일기 수정
    @Transactional
    public CommonDto updateDiary(UserPrincipal userPrincipal, UpdateDiaryReq updateDiaryReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        Diary diary = diaryRepository.findById(updateDiaryReq.getId()).orElseThrow(InvalidDiaryException::new);

        if (!diary.getUser().equals(user)) {
            throw new InvalidDiaryException();
        }

        diary.updateDiary(updateDiaryReq.getTitle(), updateDiaryReq.getContent(), updateDiaryReq.getDate(), updateDiaryReq.getEmotion(), updateDiaryReq.getStart_sleep(), updateDiaryReq.getEnd_sleep());

        UpdateDiaryRes updateDiaryRes = UpdateDiaryRes.builder()
                .changed(diary.getChanged())
                .message("꿈이 성공적으로 수정되었습니다.")
                .build();

        return new CommonDto(true, updateDiaryRes);
    }
}
