package com.capstone.Carvedream.domain.GPT.presentation;

import com.capstone.Carvedream.domain.GPT.application.GPTService;
import com.capstone.Carvedream.domain.GPT.dto.request.ChatReq;
import com.capstone.Carvedream.domain.GPT.dto.response.ChatRes;
import com.capstone.Carvedream.global.config.security.token.CurrentUser;
import com.capstone.Carvedream.global.config.security.token.UserPrincipal;
import com.capstone.Carvedream.global.payload.CommonDto;
import com.capstone.Carvedream.global.payload.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GptChat", description = "채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gptChat")
public class GPTController {

    private final GPTService gptService;

    //GPT 채팅
    @Operation(summary = "GPT 채팅", description = "GPT와 채팅을 통해 대화합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채팅 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ChatRes.class) ) } ),
            @ApiResponse(responseCode = "400", description = "채팅 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping
    public ResponseEntity<CommonDto> getChat(
            @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody ChatReq chatReq
            ) {
        return ResponseEntity.ok(gptService.getChatResponseAndSave(userPrincipal, chatReq));
    }
}
