package ru.otus.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.spring.service.QuestionnaireConductorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = QuestionnaireConductorService.class)
@ActiveProfiles("test")
public class QuestionnaireApplicationTest {

    @Autowired
    ApplicationContext context;

    @Test
    public void runApplicationContextNonNull() {
        assertNotNull(context);
    }

}
