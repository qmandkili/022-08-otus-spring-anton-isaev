package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.domain.Butterfly;

@MessagingGateway
public interface LifeCycleGateway {

    @Gateway(requestChannel = "butterflyChannel", replyChannel = "lifecycleChannel")
    Butterfly process(Butterfly butterfly);
}
