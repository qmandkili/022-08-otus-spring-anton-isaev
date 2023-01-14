package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;

public interface AdultLifeCycleService {

    Butterfly processAdultTime(Butterfly butterfly) throws InterruptedException;
}
