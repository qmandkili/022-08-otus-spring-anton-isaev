package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Question;
import ru.otus.spring.service.QuestionnaireConductorService;
import ru.otus.spring.service.FileReaderService;
import ru.otus.spring.service.IOServiceStreams;
import ru.otus.spring.service.StringToQuestionConverterServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionnaireConductorServiceTest {

    @Mock
    private IOServiceStreams ioServiceStreams;
    @Mock
    private FileReaderService readerService;
    @Mock
    private StringToQuestionConverterServiceImpl converter;
    @InjectMocks
    private QuestionnaireConductorService questionnaireService;

    private static final String QUESTION_VALUE = "question";

    @Test
    public void askOneQuestionHappyPath() {
        when(ioServiceStreams.readStringWithPrompt(any())).thenReturn("any");
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);
        spyList.add(QUESTION_VALUE);
        when(readerService.readFileAsResource()).thenReturn(spyList);
        List<Question> questions = spy(new ArrayList<>());
        Question question = mock(Question.class);
        questions.add(question);
        when(converter.convertStringListToQuestionList(spyList)).thenReturn(questions);
        questionnaireService.run();
        verify(ioServiceStreams, times(3)).readStringWithPrompt(any());
        verify(ioServiceStreams, times(3)).outputString(any());
    }

    @Test
    public void askTwoQuestionsHappyPath() {
        when(ioServiceStreams.readStringWithPrompt(any())).thenReturn("any");
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);
        spyList.add(QUESTION_VALUE);
        spyList.add(QUESTION_VALUE);
        when(readerService.readFileAsResource()).thenReturn(spyList);
        Question question = mock(Question.class);
        List<Question> questions = spy(new ArrayList<>());
        questions.add(question);
        questions.add(question);
        when(converter.convertStringListToQuestionList(spyList)).thenReturn(questions);
        questionnaireService.run();
        verify(ioServiceStreams, times(4)).readStringWithPrompt(any());
        verify(ioServiceStreams, times(4)).outputString(any());
    }

    @Test
    public void askNoQuestionHappyPath() {
        when(ioServiceStreams.readStringWithPrompt(any())).thenReturn("any");
        List<String> list = new ArrayList<>();
        List<String> spyList = spy(list);
        when(readerService.readFileAsResource()).thenReturn(spyList);
        List<Question> questions = spy(new ArrayList<>());
        when(converter.convertStringListToQuestionList(spyList)).thenReturn(questions);
        questionnaireService.run();
        verify(ioServiceStreams, times(2)).readStringWithPrompt(any());
        verify(ioServiceStreams, times(1)).outputString(any());
    }
}
