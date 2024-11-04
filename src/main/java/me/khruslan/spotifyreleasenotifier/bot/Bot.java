package me.khruslan.spotifyreleasenotifier.bot;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private static final String TAG = "Bot";

    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private final String token;
    private final TelegramClient telegramClient;

    @Autowired
    private Command.Factory commandFactory;

    public Bot(@Value("${telegram.bot.token}") String token) {
        this.token = token;
        telegramClient = new OkHttpTelegramClient(token);
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
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
