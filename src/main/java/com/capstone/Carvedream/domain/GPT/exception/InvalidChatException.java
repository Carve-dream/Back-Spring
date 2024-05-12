package com.capstone.Carvedream.domain.GPT.exception;

public class InvalidChatException extends RuntimeException {

    public InvalidChatException(){
        super("채팅 api의 응답 값이 올바르지 않습니다.");
    }

    public InvalidChatException(String error) {
        super(error);
    }
}
