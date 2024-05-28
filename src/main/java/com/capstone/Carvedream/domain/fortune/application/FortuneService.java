package com.capstone.Carvedream.domain.fortune.application;

import com.capstone.Carvedream.domain.diary.dto.response.UseGptRes;
import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import com.capstone.Carvedream.domain.fortune.domain.repository.FortuneRepository;
import com.capstone.Carvedream.domain.fortune.dto.FindFortuneRes;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FortuneService {

    private final UserRepository userRepository;
    private final FortuneRepository fortuneRepository;

    // 포춘쿠키 생성
    @Transactional
    public CommonDto createFortune(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        //TODO 포춘쿠키 로직
        //GPT 사용
        String result = "포춘쿠키 결과 들어갈 문자열";

        Fortune fortune = Fortune.builder()
                .user(user)
                .content(result)
                .build();

        fortuneRepository.save(fortune);

        return new CommonDto(true, new UseGptRes(result));
    }

    // 유저의 포춘쿠키 월별 조회
    public CommonDto findFortune(UserPrincipal userPrincipal, int year, int month) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Fortune> fortunePage = fortuneRepository.findAllByUserAndCreatedDateBetween(user, start, end);

        List<FindFortuneRes> findFortuneRes = fortunePage.stream().map(
                fortune -> FindFortuneRes.builder()
                        .id(fortune.getId())
                        .content(fortune.getContent())
                        .createAt(fortune.getCreatedDate())
                        .build()
        ).toList();

        return new CommonDto(true, findFortuneRes);
    }
}
