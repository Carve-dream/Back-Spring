package com.capstone.Carvedream.domain.user.application;

import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.dto.UpdateUserReq;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    //현재 유저 정보 조회
    public CommonDto whoAmI(UserPrincipal userPrincipal){
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        return CommonDto.builder().check(true).information(user).build();
    }

    //유저 정보 수정
    @Transactional
    public CommonDto updateUser(UserPrincipal userPrincipal, UpdateUserReq updateUserReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        user.updateUser(updateUserReq.getName(),updateUserReq.getGender(), updateUserReq.getBirthDate(), updateUserReq.getImageUrl());

        return CommonDto.builder().check(true).information(user).build();
    }
}
