package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import ru.otus.spring.config.AppProps;

@Profile("!test")
@SpringBootApplication
@EnableConfigurationProperties(AppProps.class)
public class QuestionnaireShellApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuestionnaireShellApplication.class, args);
    }

}
