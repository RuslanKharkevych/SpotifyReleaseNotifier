import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {
    private static final String PROPERTIES_FILENAME = "application.properties";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";

    public static void main(String[] args) throws TelegramApiException, IOException {
        Properties properties = readApplicationProperties();
        String botUsername = properties.getProperty(KEY_BOT_USERNAME);
        String botToken = properties.getProperty(KEY_BOT_TOKEN);

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        LongPollingBot bot = new SpotifyReleaseNotifierBot(botUsername, botToken);
        botsApi.registerBot(bot);
    }

    @NotNull
    private static Properties readApplicationProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesInputStream = classLoader.getResourceAsStream(PROPERTIES_FILENAME);

        Properties properties = new Properties();
        properties.load(propertiesInputStream);
        return properties;
    }
}
