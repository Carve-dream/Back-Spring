package com.capstone.Carvedream.domain.GPT.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.capstone.Carvedream.domain.GPT.dto.request.ChatReq;
import com.capstone.Carvedream.domain.GPT.dto.response.ChatRes;
import com.capstone.Carvedream.domain.GPT.exception.EmptyContentException;
import com.capstone.Carvedream.domain.GPT.exception.InvalidRoleException;
import com.capstone.Carvedream.domain.diary.domain.Diary;
import com.capstone.Carvedream.domain.diary.domain.repository.DiaryRepository;
import com.capstone.Carvedream.domain.diary.dto.request.UseGptReq;
import com.capstone.Carvedream.domain.diary.dto.response.UseGptRes;
import com.capstone.Carvedream.domain.diary.exception.InvalidDiaryException;
import com.capstone.Carvedream.domain.user.domain.User;
import com.capstone.Carvedream.domain.user.domain.repository.UserRepository;
import com.capstone.Carvedream.domain.user.exception.InvalidUserException;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GPTService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    @Value("${gpt.apiKey}")
    private String API_KEY;
    @Value("${gpt.model}")
    private String MODEL;
    @Value("${gpt.assistantId}")
    private String ASSISTANT_ID;
    @Value("${gpt.openAIBeta}")
    private String OPEN_AI_BETA;

    // GPT 채팅
    public CommonDto getChatResponseAndSave(UserPrincipal userPrincipal, ChatReq chatReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);
        String input = chatReq.getQuestion();
        String threadId = user.getThreadId();

        String output = sendMessageAndGetResponse(threadId, input);
        return new CommonDto(true, new ChatRes(output));
    }

    // 채팅에 메시지를 추가하고 응답 반환 (이전 대화 기억O)
    private String sendMessageAndGetResponse(String threadId, String input) {
        try {
            addMessage(threadId, input);
            createRun(threadId);
            return getResponse(threadId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 메시지 추가
    private void addMessage(String threadId, String input) throws Exception {
        String addMessageURL = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        URL url = new URL(addMessageURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("OpenAI-Beta", OPEN_AI_BETA);
            conn.setDoOutput(true);

            String jsonInputString = "{\"role\": \"user\", \"content\": \"" + input + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] inputBytes = jsonInputString.getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to add message");
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                System.out.println("Response: " + conn.getResponseMessage());
            }
        } finally {
            conn.disconnect();
        }
    }

    // 실행 생성
    private void createRun(String threadId) throws Exception {
        String createRunURL = "https://api.openai.com/v1/threads/" + threadId + "/runs";
        URL url = new URL(createRunURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("OpenAI-Beta", OPEN_AI_BETA);
            conn.setDoOutput(true);

            String jsonInputString = "{" + "\"assistant_id\": \"" + ASSISTANT_ID + "\"," + "\"instructions\": \"\"" + "}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] inputBytes = jsonInputString.getBytes("utf-8");
                os.write(inputBytes, 0, inputBytes.length);
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to create run");
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                System.out.println("createRun : " + response.toString());
                // API 응답 처리 후 1초 대기
                Thread.sleep(1000);
            }
        } finally {
            conn.disconnect();
        }
    }

    // 응답 수신
    private String getResponse(String threadId) throws Exception {
        String getMessagesURL = "https://api.openai.com/v1/threads/" + threadId + "/messages";
        URL url = new URL(getMessagesURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("OpenAI-Beta", OPEN_AI_BETA);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to get response");
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
//                System.out.println("getResponse: " + response.toString());

                // 응답을 JSON으로 파싱하고 content 추출
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray data = jsonResponse.getJSONArray("data");
                JSONObject dataObject = data.getJSONObject(0);
                String role = dataObject.getString("role");
                if (role.equals("user")) {
                    throw new InvalidRoleException();
                }
                JSONArray content = dataObject.getJSONArray("content");
                if (content.isEmpty()) {
                    throw new EmptyContentException();
                }
                JSONObject contentObject = content.getJSONObject(0);
                JSONObject text = contentObject.getJSONObject("text");
                return text.getString("value");
            }
        } finally {
            conn.disconnect();
        }
    }

    // 해몽하기
    @Transactional
    public CommonDto interpret(UserPrincipal userPrincipal, UseGptReq useGptReq) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(InvalidUserException::new);

        String result = callChatGptApi(useGptReq.getContent() + ". 이 꿈 해몽해줘.");

        if (useGptReq.getId() != 0) {
            Diary diary = diaryRepository.findByIdAndUser(userPrincipal.getId(), user).orElseThrow(InvalidDiaryException::new);
            diary.updateInterpretation(result);
        }

        return new CommonDto(true, new UseGptRes(result));
    }

    // 해몽 GPT API 호출해서 응답 반환 (이전 대화 기억X)
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
