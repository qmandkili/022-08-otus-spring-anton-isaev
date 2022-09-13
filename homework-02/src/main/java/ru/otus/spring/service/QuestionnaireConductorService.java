package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionnaireConductorService {

    private final IOServiceStreams ioServiceStreams;
    private final FileReaderService readerService;
    private final StringToQuestionConverterServiceImpl converter;

    public void run() {
        Student student = getStudentInfo();
        List<Question> questions = converter.convertStringListToQuestionList(getQuestionTextFromFile());
        askQuestions(questions);
        printAllQuestions(questions, student);
    }

    private Student getStudentInfo() {
        String name = ioServiceStreams.readStringWithPrompt("Enter your name:");
        String surname = ioServiceStreams.readStringWithPrompt("Enter your surname:");
        return new Student(name, surname);
    }

    private List<String> getQuestionTextFromFile() {
        return readerService.readFileAsResource();
    }

    private void askQuestions(List<Question> questions) {
        for (Question question : questions) {
            String answer = ioServiceStreams.readStringWithPrompt(question.getQuestionValue());
            question.setAnswerValue(answer);
        }
    }

    private void printAllQuestions(List<Question> questions, Student student) {
        ioServiceStreams.outputString(student.getName() + " " + student.getSurname());
        checkQuestions(questions);
        questions.stream()
                .forEach(question ->
                        ioServiceStreams.outputString(question.getQuestionValue() + " " + question.getAnswerValue()));
    }

    private void checkQuestions(List<Question> questions) {
        if (isTestCorrect(questions)) {
            long blankAnswers = getNumberOfBlankAnswers(questions);
            ioServiceStreams.outputString(String.format("Some answers are blank (%s/%s):",
                    blankAnswers, questions.size()));
        }
    }

    private boolean isTestCorrect(List<Question> questions) {
        return questions.stream()
                .anyMatch(question -> StringUtils.isBlank(question.getAnswerValue()));
    }

    private long getNumberOfBlankAnswers(List<Question> questions) {
        return questions.stream()
                .filter(question -> StringUtils.isBlank(question.getAnswerValue())).count();
    }
}
