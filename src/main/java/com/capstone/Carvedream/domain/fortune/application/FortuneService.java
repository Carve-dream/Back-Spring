package com.capstone.Carvedream.domain.fortune.application;

import com.capstone.Carvedream.domain.diary.domain.repository.DiaryRepository;
import com.capstone.Carvedream.domain.fortune.domain.Fortune;
import com.capstone.Carvedream.domain.fortune.domain.repository.FortuneRepository;
import com.capstone.Carvedream.domain.fortune.dto.FindFortuneRes;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FortuneService {

    private final UserRepository userRepository;
    private final FortuneRepository fortuneRepository;
    private final DiaryRepository diaryRepository;

    @Value("${gpt.apiKey}")
    private String API_KEY;
    @Value("${gpt.model}")
    private String MODEL;

    // 포춘쿠키 생성
    @Transactional
    public CommonDto createFortune(UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        if (fortuneRepository.existsByUserAndCreatedDateBetween(user, startOfDay, endOfDay)) {
            return new CommonDto(false, "오늘의 포춘쿠키는 이미 생성되었습니다.");
        }

        List<String> recentInterpretations = diaryRepository.findTop5InterpretationsByUserOrderByIdDesc(user);
        String fortuneContent = getFortuneFromInterpretations(recentInterpretations);

        Fortune fortune = Fortune.builder()
                .user(user)
                .content(fortuneContent)
                .build();

        Fortune savedFortune = fortuneRepository.save(fortune);

        return new CommonDto(true, FindFortuneRes.builder()
                .id(savedFortune.getId())
                .createAt(savedFortune.getCreatedDate())
                .content(savedFortune.getContent())
                .build());
    }

    // GPT에게 요청
    private String getFortuneFromInterpretations(List<String> recentInterpretations) {
        String prompt = "다음 해몽 기록을 바탕으로 사용자에게 한 문장으로 간단하게 조언 해주세요:\n";
        for (String interpretation : recentInterpretations) {
            prompt += interpretation + "\n";
        }

        return callChatGptApi(prompt);
    }


    // 유저의 모든 포춘쿠키 조회
    public CommonDto findAllFortune(UserPrincipal userPrincipal, Integer page) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        Page<Fortune> fortunePage = fortuneRepository.findAllByUser(user, PageRequest.of(page, 10));

        List<FindFortuneRes> findFortuneRes = fortunePage.stream().map(
                fortune -> FindFortuneRes.builder()
                        .id(fortune.getId())
                        .content(fortune.getContent())
                        .createAt(fortune.getCreatedDate())
                        .build()
        ).toList();

        return new CommonDto(true, findFortuneRes);
    }

    // 포춘쿠키 생성을 위해 GPT API 호출해서 응답 반환
    private String callChatGptApi(String prompt) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", MODEL);

            JSONArray messages = new JSONArray();
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", prompt);
            messages.put(message);

            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(requestBody.toString().getBytes());
            outputStream.flush();
            outputStream.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            conn.disconnect();

            System.out.println("API Response: " + response);

            // JSON 파싱
            JSONObject json = new JSONObject(response.toString());
            JSONArray choices = json.getJSONArray("choices");
            if (!choices.isEmpty()) {
                JSONObject firstChoice = choices.getJSONObject(0);
                String content = firstChoice.getJSONObject("message").getString("content").trim();
                return content;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}