package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.service.CsvFileReaderServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CsvFileReaderServiceTest {

    @Mock
    private CsvFileReaderServiceImpl readerService;

    @Test
    public void setFileNameTest() {
        readerService.setFileName(any());
        verify(readerService).setFileName(any());
    }
}
