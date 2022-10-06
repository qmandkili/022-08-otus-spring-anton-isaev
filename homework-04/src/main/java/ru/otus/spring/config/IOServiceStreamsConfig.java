package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.service.IOServiceStreams;

@Configuration
public class IOServiceStreamsConfig {

    @Bean
    public IOServiceStreams ioServiceStreams() {
        return new IOServiceStreams(System.out, System.in);
    }
}
