package me.khruslan.spotifyreleasenotifier.telegram;

import me.khruslan.spotifyreleasenotifier.telegram.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class TelegramBot implements SpringLongPollingBot {
    private final TelegramConfig telegramConfig;
    private final MessageHandler messageHandler;

    @Autowired
    public TelegramBot(TelegramConfig telegramConfig, MessageHandler messageHandler) {
        this.telegramConfig = telegramConfig;
        this.messageHandler = messageHandler;
    }

    @Override
    public String getBotToken() {
        return telegramConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return messageHandler;
    }
}
