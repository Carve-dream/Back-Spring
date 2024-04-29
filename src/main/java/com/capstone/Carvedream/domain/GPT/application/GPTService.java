package com.capstone.Carvedream.domain.GPT.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.capstone.Carvedream.domain.GPT.domain.GPTAnswer;
import com.capstone.Carvedream.domain.GPT.domain.GPTQuestion;
import com.capstone.Carvedream.domain.GPT.domain.repository.GPTAnswerRepository;
import com.capstone.Carvedream.domain.GPT.domain.repository.GPTQuestionRepository;
import com.capstone.Carvedream.domain.GPT.dto.request.ChatReq;
import com.capstone.Carvedream.domain.GPT.dto.response.ChatRes;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GPTService {

    private final GPTQuestionRepository questionRepository;
    private final GPTAnswerRepository answerRepository;
    private final UserRepository userRepository;

    @Value("${gpt.apiKey}")
    private String API_KEY;
    @Value("${gpt.model}")
    private String MODEL;

    //GPT 채팅
    public CommonDto getChatResponseAndSave(UserPrincipal userPrincipal, ChatReq chatReq) {
        userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        String input = chatReq.getQuestion();

        String output = callChatGptApi(input);

        GPTQuestion question = GPTQuestion.builder()
                .content(input)
                .build();
        questionRepository.save(question);

        GPTAnswer answer = GPTAnswer.builder()
                .content(output)
                .question(question)
                .build();
        answerRepository.save(answer);

        return new CommonDto(true, new ChatRes(output));
    }

    //GPT API 호출해서 응답 가져오는 메소드
    private String callChatGptApi(String input) {
        try {
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

            String requestBody = "{\"model\": \"" + MODEL + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}], \"temperature\": 0.5}";
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(requestBody.getBytes());
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

            // JSON 파싱
            JSONObject json = new JSONObject(response.toString());
            JSONArray choices = json.getJSONArray("choices");
            if (choices != null && choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0);
                JSONObject message = firstChoice.getJSONObject("message");
                String content = message.getString("content");
                return content;
            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
