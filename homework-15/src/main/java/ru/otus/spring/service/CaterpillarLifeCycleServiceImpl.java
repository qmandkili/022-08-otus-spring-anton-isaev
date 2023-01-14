package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.LifecycleState;

import java.util.Random;

@Service
public class CaterpillarLifeCycleServiceImpl implements CaterpillarLifeCycleService {

    @Override
    public Butterfly processCaterpillarTime(Butterfly butterfly) throws InterruptedException {
        System.out.println(String.format("Butterfly %s broke the shell", butterfly.getName()));
        Thread.sleep(1000 + new Random().nextInt(10) * 100);
        butterfly.setState(LifecycleState.CATERPILLAR);
        System.out.println(String.format("Butterfly %s is caterpillar now", butterfly.getName()));
        return butterfly;
    }
}
