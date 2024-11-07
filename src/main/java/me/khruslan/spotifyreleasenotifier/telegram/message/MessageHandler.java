package me.khruslan.spotifyreleasenotifier.telegram.message;

import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.telegram.command.CommandFactory;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class MessageHandler implements LongPollingSingleThreadUpdateConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    private final CommandFactory commandFactory;
    private final TelegramService telegramService;

    @Autowired
    public MessageHandler(TelegramService telegramService, CommandFactory commandFactory) {
        this.telegramService = telegramService;
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
        var credentials = new TelegramCredentials(message.getFrom().getId(), message.getChatId());
        var command = commandFactory.create(message.getText(), credentials);
        logger.debug("Executing command: {}", command);

        var answer = command.execute();
        logger.debug("Command completed with answer: {}", answer);
        telegramService.sendMessage(message.getChatId(), answer.getMessage());
    }
}
