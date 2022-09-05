package ru.otus.spring.service;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvFileReaderServiceImpl implements FileReaderService {

    private String fileName;

    private final static String SEPARATOR = ";";

    @Override
    public void readFileAsResource() {
        InputStream is = getFileFromResourceAsStream(fileName);
        printInputStream(is);
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

    private static void printInputStream(InputStream is) {
        try (InputStreamReader streamReader =
                     new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            String[] tempArr;
            System.out.println("Question and answer:");
            while((line = reader.readLine()) != null) {
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                tempArr = line.split(SEPARATOR);
                for(String tempStr : tempArr) {
                    System.out.print(tempStr + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
