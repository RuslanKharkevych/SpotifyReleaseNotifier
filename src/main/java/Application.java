import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Application {
    private static final String BOT_PROPERTIES_FILENAME = "telegrambot.properties";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws TelegramApiException, IOException {
        logger.info("Starting application");

        logger.info("Reading {}", BOT_PROPERTIES_FILENAME);
        Properties botProperties = readTelegramBotProperties();
        String botUsername = botProperties.getProperty(KEY_BOT_USERNAME);
        String botToken = botProperties.getProperty(KEY_BOT_TOKEN);

        logger.info("Registering {}", SpotifyReleaseNotifierBot.class.getSimpleName());
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        LongPollingBot bot = new SpotifyReleaseNotifierBot(botUsername, botToken);
        botsApi.registerBot(bot);

        logger.info("Bot session started");
    }

    @NotNull
    private static Properties readTelegramBotProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesInputStream = classLoader.getResourceAsStream(BOT_PROPERTIES_FILENAME);

        Properties properties = new Properties();
        properties.load(propertiesInputStream);
        return properties;
    }
}
