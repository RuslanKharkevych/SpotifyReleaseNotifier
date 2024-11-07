package me.khruslan.spotifyreleasenotifier.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class TelegramService {
    private static final Logger logger = LoggerFactory.getLogger(TelegramService.class);

    private final TelegramClient telegramClient;

    @Autowired
    public TelegramService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void sendMessage(Long chatId, String message) {
        var sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        try {
            telegramClient.execute(sendMessage);
            logger.debug("Message sent: {}", sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message", e);
        }
    }
}
