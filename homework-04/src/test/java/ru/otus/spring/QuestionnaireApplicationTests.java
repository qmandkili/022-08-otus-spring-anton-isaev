package ru.otus.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.FileReaderService;
import ru.otus.spring.service.IOServiceStreams;
import ru.otus.spring.service.QuestionnaireConductorService;
import ru.otus.spring.service.StringToQuestionConverterServiceImpl;
import ru.otus.spring.service.utils.MessageLocalizerService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionnaireApplicationTests {

    @Mock
    private IOServiceStreams ioServiceStreams;
    @Mock
    private FileReaderService readerService;
    @Mock
    private StringToQuestionConverterServiceImpl converter;
    @Mock
    private MessageLocalizerService localizerService;
    @InjectMocks
    private QuestionnaireConductorService questionnaireService;

    private static final String QUESTION_VALUE = "question";

    @Test
    public void askOneQuestionHappyPath() {
        when(ioServiceStreams.readStringWithPrompt(any())).thenReturn("any");
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);
        spyList.add(QUESTION_VALUE);
        when(readerService.readLocalizedFileAsResource()).thenReturn(spyList);

        when(ioServiceStreams.readStringWithPrompt(any())).thenReturn("any");
        List<Question> questions = spy(new ArrayList<>());
        Question question = mock(Question.class);
        questions.add(question);
        when(converter.convertStringListToQuestionList(spyList)).thenReturn(questions);
        questionnaireService.run();
        verify(ioServiceStreams, times(3)).readStringWithPrompt(any());
        verify(ioServiceStreams, times(3)).outputString(any());
    }
}
