package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;

public interface CocoonLifeCycleService {

    Butterfly processCocoonTime(Butterfly butterfly) throws InterruptedException;
}
