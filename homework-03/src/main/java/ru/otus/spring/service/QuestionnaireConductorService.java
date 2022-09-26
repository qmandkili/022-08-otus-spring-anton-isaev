package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.service.utils.MessageLocalizerService;

import java.util.List;

@Profile("!test")
@Service
@RequiredArgsConstructor
public class QuestionnaireConductorService  implements CommandLineRunner {

    private final IOServiceStreams ioServiceStreams;
    private final FileReaderService readerService;
    private final StringToQuestionConverterServiceImpl converter;
    private final MessageLocalizerService messageLocalizerService;

    private static final String USER_NAME_MESSAGE_KEY = "user.name";
    private static final String USER_SURNAME_MESSAGE_KEY = "user.surname";
    private static final String ANSWERS_BLANK_MESSAGE_KEY = "answers.blank";

    @Override
    public void run(String... args) {
        Student student = getStudentInfo();
        List<Question> questions = converter.convertStringListToQuestionList(getQuestionTextFromFile());
        askQuestions(questions);
        printAllQuestions(questions, student);
    }

    private Student getStudentInfo() {
        String name = ioServiceStreams.readStringWithPrompt(
                messageLocalizerService.localizeMessage(USER_NAME_MESSAGE_KEY));
        String surname = ioServiceStreams.readStringWithPrompt(
                messageLocalizerService.localizeMessage(USER_SURNAME_MESSAGE_KEY));
        return new Student(name, surname);
    }

    private List<String> getQuestionTextFromFile() {
        return readerService.readLocalizedFileAsResource();
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
            ioServiceStreams.outputString(messageLocalizerService.localizeMessage(
                    ANSWERS_BLANK_MESSAGE_KEY, new String[]{
                            String.valueOf(blankAnswers), String.valueOf(questions.size())}));
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
