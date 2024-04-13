package com.capstone.Carvedream.domain.diary.exception;

public class InvalidDiaryException  extends RuntimeException {

    public InvalidDiaryException(){
        super("일기가 올바르지 않습니다.");
    }
}
