package me.khruslan.spotifyreleasenotifier.bot;

import me.khruslan.spotifyreleasenotifier.bot.message.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class Bot implements SpringLongPollingBot {
    private final BotConfig config;
    private final MessageHandler messageHandler;

    @Autowired
    public Bot(BotConfig config, MessageHandler messageHandler) {
        this.config = config;
        this.messageHandler = messageHandler;
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return messageHandler;
    }
}
