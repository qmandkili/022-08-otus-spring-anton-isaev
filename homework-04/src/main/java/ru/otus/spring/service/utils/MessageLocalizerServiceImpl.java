package ru.otus.spring.service.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.spring.config.AppProps;

@Service
@RequiredArgsConstructor
public class MessageLocalizerServiceImpl implements MessageLocalizerService {

    private final MessageSource messageSource;
    private final AppProps appProps;

    public String localizeMessage(String key) {
        return messageSource.getMessage(key, null, appProps.getLocale());
    }

    public String localizeMessage(String key, String[] args) {
        return messageSource.getMessage(key, args, appProps.getLocale());
    }
}
