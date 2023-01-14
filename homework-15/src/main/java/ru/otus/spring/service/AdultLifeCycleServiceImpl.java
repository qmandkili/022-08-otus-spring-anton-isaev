package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.domain.LifecycleState;

import java.util.Random;

@Service
public class AdultLifeCycleServiceImpl implements AdultLifeCycleService {

    @Override
    public Butterfly processAdultTime(Butterfly butterfly) throws InterruptedException {
        System.out.println("Wings emerged from the cocoon");
        Thread.sleep(1000 + new Random().nextInt(10) * 100);
        butterfly.setState(LifecycleState.ADULT);
        System.out.println(String.format("Wow. Look at this. Now it is a beautiful butterfly %s", butterfly.getName()));
        return butterfly;
    }
}
