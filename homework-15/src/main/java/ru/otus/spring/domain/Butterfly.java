package ru.otus.spring.domain;

import lombok.Data;

@Data
public class Butterfly {

    private String name;
    private LifecycleState state;

    public Butterfly(String name) {
        this.name = name;
    }
}
