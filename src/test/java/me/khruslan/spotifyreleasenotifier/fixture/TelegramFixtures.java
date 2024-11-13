package me.khruslan.spotifyreleasenotifier.fixture;

import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.telegram.command.Command;
import me.khruslan.spotifyreleasenotifier.telegram.command.UnknownCommand;
import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static me.khruslan.spotifyreleasenotifier.fixture.SpotifyFixtures.AUTH_URL;

public class TelegramFixtures {
    // region Config & Credentials
    public static final String TELEGRAM_BOT_TOKEN = "42e7cf72-837c-47e1-b2b2-e4228121ffdd";
    public static final String TELEGRAM_BOT_URL = "https://t.me/test_bot";
    public static final long TELEGRAM_USER_ID = 435319L;
    public static final long TELEGRAM_CHAT_ID = 365835L;

    public static final TelegramCredentials TELEGRAM_CREDENTIALS =
            new TelegramCredentials(TELEGRAM_USER_ID, TELEGRAM_CHAT_ID);
    // endregion Config & Credentials

    // region Messages & Commands
    public static final String MESSAGE_TEXT = "Hello!";
    public static final String UNKNOWN_COMMAND_NAME = MESSAGE_TEXT;
    public static final Command UNKNOWN_COMMAND = new UnknownCommand(UNKNOWN_COMMAND_NAME);
    public static final String LOGIN_URL_MESSAGE = String.format(Messages.LOGIN_URL, AUTH_URL);
    public static final String UNRECOGNIZED_COMMAND_MESSAGE =
            String.format(Messages.UNRECOGNIZED_COMMAND, UNKNOWN_COMMAND_NAME);

    public static final SendMessage SEND_MESSAGE = SendMessage.builder()
            .chatId(TELEGRAM_CHAT_ID)
            .text(MESSAGE_TEXT)
            .build();

    public static final Message MESSAGE = Message.builder()
            .text(MESSAGE_TEXT)
            .from(User.builder().id(TELEGRAM_USER_ID).firstName("John Doe").isBot(false).build())
            .chat(Chat.builder().id(TELEGRAM_CHAT_ID).type("PRIVATE").build())
            .build();

    public static final Command MOCK_COMMAND = new Command() {
        @Override
        public String execute() {
            return MOCK_ANSWER;
        }
    };

    public static final String MOCK_ANSWER = "Hi!";
    // endregion Messages & Commands

    // region Updates
    public static Update mockUpdateWithoutMessage() {
        return new Update();
    }

    public static Update mockUpdateWithNonTextMessage() {
        var update = new Update();
        update.setMessage(new Message());
        return update;
    }

    public static Update mockUpdateWithTextMessage() {
        var update = new Update();
        update.setMessage(MESSAGE);
        return update;
    }
    // endregion Updates
}