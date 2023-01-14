package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;

public interface CaterpillarLifeCycleService {

    Butterfly processCaterpillarTime(Butterfly butterfly) throws InterruptedException;
}
