import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

class SpotifyReleaseNotifierBot extends TelegramLongPollingBot {
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
        // Updates are not supported yet
    }
}
