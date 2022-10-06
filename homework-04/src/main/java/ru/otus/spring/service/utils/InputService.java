package ru.otus.spring.service.utils;

public interface InputService {

    int readInt();

    int readIntWithPrompt(String prompt);

    String readStringWithPrompt(String prompt);
}
