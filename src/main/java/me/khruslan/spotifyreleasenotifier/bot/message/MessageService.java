package me.khruslan.spotifyreleasenotifier.bot.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final TelegramClient telegramClient;

    @Autowired
    public MessageService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void sendMessage(Long chatId, String message) {
        var sendMessage = buildSendMessageMethod(chatId, message);
        try {
            telegramClient.execute(sendMessage);
            logger.debug("Message sent: {}", message);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message", e);
        }
    }

    private SendMessage buildSendMessageMethod(Long chatId, String message) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
    }
}
