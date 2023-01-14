package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.LifecycleState;

import java.util.Random;

@Service
public class EggCycleServiceImpl implements EggCycleService {

    @Override
    public Butterfly processEggTime(Butterfly butterfly) throws InterruptedException {
        //System.out.println(String.format("Butterfly %s was born", butterfly.getName()));
        Thread.sleep(1000 + new Random().nextInt(10) * 100);
        butterfly.setState(LifecycleState.EGG);
        System.out.println(String.format("Butterfly %s is egg", butterfly.getName()));
        return butterfly;
    }
}
