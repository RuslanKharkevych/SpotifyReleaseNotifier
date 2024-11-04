package me.khruslan.spotifyreleasenotifier.bot.message;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.command.Command;
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
    private final Command.Factory commandFactory;

    @Autowired
    public MessageHandler(TelegramClient telegramClient, Command.Factory commandFactory) {
        this.telegramClient = telegramClient;
        this.commandFactory = commandFactory;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage()) {
            logger.info("Skipped update without message: updateId={}", update.getUpdateId());
            return;
        }

        Message message = update.getMessage();
        if (!message.hasText()) {
            logger.info("Skipped message without text: messageId={}", message.getMessageId());
            return;
        }

        String messageText = message.getText();
        logger.info("Message received: {}", messageText);

        Command command = commandFactory.create(messageText);
        logger.info("Executing command: {}", command);
        Answer answer = command.execute();
        logger.info("Command completed with answer: {}", answer);
        sendMessage(message.getChatId(), answer.getMessage());
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();

        try {
            telegramClient.execute(sendMessage);
            logger.info("Message sent: {}", message);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message", e);
        }
    }
}
