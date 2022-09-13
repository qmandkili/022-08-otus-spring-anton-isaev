package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvFileReaderServiceImpl implements FileReaderService {

    @Value("${service.fileName}")
    private String fileName;

    private final static String SEPARATOR = ";";

    @Override
    public List<String> readFileAsResource() {
        InputStream is = getFileFromResourceAsStream(fileName);
        return readTextFromFile(is);
    }

    private InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }
    }

    private List<String> readTextFromFile(InputStream is) {
        List<String> textFromFile = new ArrayList<>();
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            String[] tempArr;
            while((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                tempArr = line.split(SEPARATOR);
                textFromFile.add(tempArr[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFromFile;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
