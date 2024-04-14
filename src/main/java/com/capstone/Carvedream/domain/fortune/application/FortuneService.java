package com.capstone.Carvedream.domain.fortune.application;

import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.diary.dto.request.CreateDiaryReq;
import com.capstone.Carvedream.domain.diary.dto.response.CreateDiaryRes;
import com.capstone.Carvedream.domain.diary.dto.response.UseGptRes;
import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import com.capstone.Carvedream.domain.fortune.domain.repository.FortuneRepository;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        String result = "포춘쿠키 결과 들어갈 문자열";

        Fortune fortune = Fortune.builder()
                .user(user)
                .content(result)
                .build();

        fortuneRepository.save(fortune);

        return new CommonDto(true, new UseGptRes(result));
    }
}
