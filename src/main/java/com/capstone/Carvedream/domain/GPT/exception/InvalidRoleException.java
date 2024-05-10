package com.capstone.Carvedream.domain.GPT.exception;

public class InvalidRoleException extends RuntimeException {

    public InvalidRoleException(){
        super("채팅 api의 응답 값이 올바르지 않습니다. 통신 속도 원인일 수 있습니다.");
    }
}
