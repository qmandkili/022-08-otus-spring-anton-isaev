package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface ObjectsToQuestionConverterService extends ConverterService {

    List<Question> convertStringListToQuestionList(List<String> questions);
}
