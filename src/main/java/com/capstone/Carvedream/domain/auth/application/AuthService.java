package com.capstone.Carvedream.domain.auth.application;

import com.capstone.Carvedream.domain.auth.domain.Token;
import com.capstone.Carvedream.domain.auth.domain.repository.TokenRepository;
import com.capstone.Carvedream.domain.auth.dto.*;
import com.capstone.Carvedream.domain.auth.dto.request.RefreshTokenReq;
import com.capstone.Carvedream.domain.auth.dto.request.SignInReq;
import com.capstone.Carvedream.domain.auth.dto.request.SignUpReq;
import com.capstone.Carvedream.domain.auth.dto.response.AuthRes;
import com.capstone.Carvedream.domain.user.domain.Role;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.global.DefaultAssert;
import com.capstone.Carvedream.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;



@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    @Value("${gpt.apiKey}")
    private String API_KEY;
    @Value("${gpt.openAIBeta}")
    private String OPEN_AI_BETA;

    //로그인
    @Transactional
    public AuthRes signin(SignInReq signInRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();
        tokenRepository.save(token);
        return AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();
    }

    //회원가입
    @Transactional
    public Message signup(SignUpReq signUpRequest) throws Exception {
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "이미 존재하는 이메일입니다.");

        User user = User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .role(Role.ADMIN)
                        .birthDate(signUpRequest.getBirthDate())
                        .gender(signUpRequest.getGender())
                        .threadId(createThread())
                        .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(user.getId()).toUri();
        return Message.builder().message("회원가입에 성공하였습니다.").build();
    }

    //토큰 재발급
    @Transactional
    public AuthRes refresh(RefreshTokenReq tokenRefreshRequest){
        //1차 검증
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4. refresh token 정보 값을 업데이트 한다.
        //시간 유효성 확인
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        return AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();
    }

    //로그아웃
    @Transactional
    public Message signout(RefreshTokenReq tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4 token 정보를 삭제한다.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        return Message.builder().message("로그아웃 하였습니다.").build();
    }

    //탈퇴
    @Transactional
    public Message cancel(RefreshTokenReq tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        userRepository.deleteByEmail(token.get().getUserEmail());
        tokenRepository.delete(token.get());
        return Message.builder().message("탈퇴 하였습니다.").build();
    }

    //유저 확인
    private boolean valid(String refreshToken){

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "로그인이 필요합니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }

    private String createThread() throws Exception {

        String createThreadURL = "https://api.openai.com/v1/threads";

        // URL 객체 생성
        URL url = new URL(createThreadURL);

        // HttpURLConnection 객체 생성 및 설정
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("OpenAI-Beta", OPEN_AI_BETA);
        conn.setDoOutput(true);

        // 빈 JSON 데이터를 요청 본문에 작성
        String jsonInputString = "{}";
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        String threadId = null;

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                // 응답을 JSON으로 파싱하고 thread ID 추출
                JSONObject jsonResponse = new JSONObject(response.toString());
                threadId = jsonResponse.optString("id", "No Thread ID Found");
                System.out.println("Thread ID: " + threadId);
            }
        } else {
            System.out.println("Error Response Code: " + responseCode);
        }

        // 리소스 해제
        conn.disconnect();
        return threadId;
    }
}
