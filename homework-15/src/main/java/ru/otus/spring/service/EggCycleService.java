package ru.otus.spring.service;

import ru.otus.spring.domain.Butterfly;

public interface EggCycleService {

    Butterfly processEggTime(Butterfly butterfly) throws InterruptedException;
}
