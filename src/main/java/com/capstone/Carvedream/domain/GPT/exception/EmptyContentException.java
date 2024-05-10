package com.capstone.Carvedream.domain.GPT.exception;

public class EmptyContentException extends RuntimeException {

    public EmptyContentException(){
        super("응답 값이 비어있습니다.");
    }
}
