package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.domain.Butterfly;
import ru.otus.spring.integration.LifeCycleGateway;

import java.util.List;
import java.util.Random;

@ShellComponent
@RequiredArgsConstructor
public class ShellCommandAccepter {

    private final LifeCycleGateway lifeCycleGateway;

    private static List<String> BUTTERFLY_NAMES = List.of("Hesperiidae", "Lycaenidae", "Nymphalidae", "Papilionidae");

    @ShellMethod(value = "startButterflyLifeCycle", key = "s")
    public void start() throws InterruptedException {
        while (true) {
            Thread.sleep(4000);
            Thread thread = new Thread(() -> {
                Butterfly butterfly = new Butterfly(BUTTERFLY_NAMES.get(new Random().nextInt(3)));
                System.out.println(String.format("New Butterfly %s was born", butterfly.getName()));
                Butterfly adultButterfly = lifeCycleGateway.process(butterfly);
                System.out.println("Look at butterfly: " + adultButterfly.toString());
            });
            thread.start();
        }
    }
}
