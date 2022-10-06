package ru.otus.spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommandsAccepter {

    private final QuestionnaireConductorService questionnaireConductorService;

    @ShellMethod(value = "Start questionnaire")
    public void start() {
        questionnaireConductorService.run();
    }
}
