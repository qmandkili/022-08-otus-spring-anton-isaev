package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("test")
public class QuestionnaireApplicationStarter {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(QuestionnaireApplicationStarter.class);
        app.setLogStartupInfo(false);
        app.run(args);
    }
}
