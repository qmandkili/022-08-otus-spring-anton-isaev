package ru.otus.spring.service.utils;

public interface MessageLocalizerService {

    String localizeMessage(String key);

    String localizeMessage(String key, String[] args);
}
