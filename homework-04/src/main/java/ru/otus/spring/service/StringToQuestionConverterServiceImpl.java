package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Question;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StringToQuestionConverterServiceImpl implements ObjectsToQuestionConverterService {

    public Question convertStringToQuestion(String question) {
        return new Question(question, null);
    }

    public List<Question> convertStringListToQuestionList(List<String> questions) {
        return questions.stream()
                .map(this::convertStringToQuestion)
                .collect(Collectors.toList());
    }
}
