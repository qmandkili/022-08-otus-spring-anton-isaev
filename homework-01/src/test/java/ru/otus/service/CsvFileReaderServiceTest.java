package ru.otus.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.otus.spring.service.CsvFileReaderServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CsvFileReaderServiceTest {

    @Mock
    private CsvFileReaderServiceImpl readerService;

    @Test
    public void setFileNameTest() {
        readerService.setFileName(any());
        verify(readerService).setFileName(any());
    }
}
