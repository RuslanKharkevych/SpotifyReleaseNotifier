package me.khruslan.spotifyreleasenotifier.bot.message;

import me.khruslan.spotifyreleasenotifier.bot.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class MessageHandler implements LongPollingSingleThreadUpdateConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final TelegramClient telegramClient;
    private final CommandFactory commandFactory;

    @Autowired
    public MessageHandler(TelegramClient telegramClient, CommandFactory commandFactory) {
        this.telegramClient = telegramClient;
        this.commandFactory = commandFactory;
    }

    @Override
    public void consume(Update update) {
        logger.debug("Handling update: {}", update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update.getMessage());
        } else {
            logger.debug("Skipping update without text message");
        }
    }

    private void handleTextMessage(Message message) {
        var command = commandFactory.create(message.getText());
        logger.debug("Executing command: {}", command);
        var answer = command.execute();
        logger.debug("Command completed with answer: {}", answer);
        sendMessage(message.getChatId(), answer.getMessage());
    }

    private void sendMessage(Long chatId, String message) {
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
