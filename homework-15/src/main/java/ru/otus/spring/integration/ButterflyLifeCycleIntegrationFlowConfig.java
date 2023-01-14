package ru.otus.spring.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.service.AdultLifeCycleService;
import ru.otus.spring.service.CaterpillarLifeCycleService;
import ru.otus.spring.service.CocoonLifeCycleService;
import ru.otus.spring.service.EggCycleService;

@Configuration
@RequiredArgsConstructor
public class ButterflyLifeCycleIntegrationFlowConfig {

    private final EggCycleService eggCycleService;
    private final CaterpillarLifeCycleService caterpillarLifeCycleService;
    private final CocoonLifeCycleService cocoonLifeCycleService;
    private final AdultLifeCycleService adultLifeCycleService;

    @Bean
    public QueueChannel userChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public PublishSubscribeChannel passportChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER )
    public PollerMetadata poller () {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).get() ;
    }

    @Bean
    public IntegrationFlow workFlow() {
        return IntegrationFlows.from("butterflyChannel")
                .handle(eggCycleService, "processEggTime")
                .handle(caterpillarLifeCycleService,"processCaterpillarTime")
                .handle(cocoonLifeCycleService,"processCocoonTime")
                .handle(adultLifeCycleService,"processAdultTime")
                .channel("lifecycleChannel")
                .get();
    }
}
