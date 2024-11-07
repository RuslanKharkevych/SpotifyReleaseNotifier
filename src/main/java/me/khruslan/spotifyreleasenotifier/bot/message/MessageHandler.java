package me.khruslan.spotifyreleasenotifier.bot.message;

import me.khruslan.spotifyreleasenotifier.bot.command.CommandFactory;
import me.khruslan.spotifyreleasenotifier.user.UserMetadata;
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
    private final MessageService messageService;

    @Autowired
    public MessageHandler(MessageService messageService, CommandFactory commandFactory) {
        this.messageService = messageService;
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
        var metadata = new UserMetadata(message.getFrom().getId(), message.getChatId());
        var command = commandFactory.create(message.getText(), metadata);
        logger.debug("Executing command: {}", command);
        var answer = command.execute();
        logger.debug("Command completed with answer: {}", answer);
        messageService.sendMessage(message.getChatId(), answer.getMessage());
    }
}
