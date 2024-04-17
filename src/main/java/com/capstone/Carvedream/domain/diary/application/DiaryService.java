package com.capstone.Carvedream.domain.diary.application;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.diary.domain.repository.DiaryRepository;
import com.capstone.Carvedream.domain.diary.dto.request.CreateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.request.UpdateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.response.CalendarRes;
import com.capstone.Carvedream.domain.diary.dto.response.CreateDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.FindDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.UpdateDiaryRes;
import com.capstone.Carvedream.domain.diary.exception.InvalidDiaryException;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

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

    // 꿈 일기 삭제
    @Transactional
    public CommonDto deleteDiary(UserPrincipal userPrincipal, Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        Diary diary = diaryRepository.findById(id).orElseThrow(InvalidDiaryException::new);

        if (!diary.getUser().equals(user)) {
            throw new InvalidDiaryException();
        }

        diaryRepository.delete(diary);

        return new CommonDto(true, Message.builder().message("꿈이 삭제되었습니다.").build());
    }

    // id로 꿈 일기 조회
    public CommonDto findDiary(UserPrincipal userPrincipal, Long id) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        Diary diary = diaryRepository.findById(id).orElseThrow(InvalidDiaryException::new);

        if (!diary.getUser().equals(user)) {
            throw new InvalidDiaryException();
        }

        FindDiaryRes findDiaryRes = FindDiaryRes.builder()
                .id(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .date(diary.getDate())
                .emotion(diary.getEmotion())
                .image_url(diary.getImage_url())
                .end_sleep(diary.getEnd_sleep())
                .start_sleep(diary.getStart_sleep())
                .interpretation(diary.getInterpretation())
                .build();

        return new CommonDto(true, findDiaryRes);
    }

    // 유저의 모든 꿈 일기 조회
    public CommonDto findAllDiary(UserPrincipal userPrincipal, Integer page) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        Page<Diary> diaryPage = diaryRepository.findAllByUser(user, PageRequest.of(page, 10));

        List<FindDiaryRes> findDiaryRes = diaryPage.stream().map(
                diary -> FindDiaryRes.builder()
                    .id(diary.getId())
                    .title(diary.getTitle())
                    .content(diary.getContent())
                    .date(diary.getDate())
                    .emotion(diary.getEmotion())
                    .image_url(diary.getImage_url())
                    .end_sleep(diary.getEnd_sleep())
                    .start_sleep(diary.getStart_sleep())
                    .interpretation(diary.getInterpretation())
                    .build()
        ).toList();

        return new CommonDto(true, findDiaryRes);
    }

    //캘린더에 감정 이모티콘 불러오기
    public CommonDto getEmotionCalendar(UserPrincipal userPrincipal, Integer year, Integer month) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        List<Diary> diaryList = getDiariesForMonth(user, year, month);

        List<CalendarRes> calendarRes = diaryList.stream().map(
                diary -> CalendarRes.builder()
                        .id(diary.getId())
                        .date(diary.getDate())
                        .emotion(diary.getEmotion())

                        .build()
        ).toList();

        return new CommonDto(true, calendarRes);
    }

    private List<Diary> getDiariesForMonth(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return diaryRepository.findAllByUserAndDateBetween(user, start, end);
    }

}
