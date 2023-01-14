package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.LifecycleState;

import java.util.Random;

@Service
public class CocoonLifeCycleServiceImpl implements CocoonLifeCycleService {

    @Override
    public Butterfly processCocoonTime(Butterfly butterfly) throws InterruptedException {
        System.out.println(String.format("Caterpillar %s wove the cocoon", butterfly.getName()));
        Thread.sleep(1000 + new Random().nextInt(10) * 100);
        butterfly.setState(LifecycleState.CATERPILLAR);
        System.out.println(String.format("Caterpillar %s is hiding in cocoon", butterfly.getName()));
        return butterfly;
    }
}
