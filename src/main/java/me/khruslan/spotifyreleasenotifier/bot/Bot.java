package me.khruslan.spotifyreleasenotifier.bot;

import me.khruslan.spotifyreleasenotifier.bot.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;

public class Bot extends TelegramLongPollingBot {
    private static final String TAG = "Bot";

    private static final Logger logger = LoggerFactory.getLogger(TAG);

    private final String username;
    private final Command.Factory commandFactory = new Command.Factory();

    public Bot(String username, String token) {
        super(token);
        this.username = username;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
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
            execute(sendMessage);
            logger.info("Message sent: {}", message);
        } catch (TelegramApiException e) {
            logger.error("Failed to send message", e);
        }
    }
}
