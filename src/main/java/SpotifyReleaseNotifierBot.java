import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

class SpotifyReleaseNotifierBot extends TelegramLongPollingBot {
    static final Logger logger = LoggerFactory.getLogger(SpotifyReleaseNotifierBot.class);

    final String username;

    SpotifyReleaseNotifierBot(String username, String token) {
        super(token);
        this.username = username;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info(update.getMessage().getText());
    }
}
