package me.khruslan.spotifyreleasenotifier;

import me.khruslan.spotifyreleasenotifier.bot.Bot;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {
    private static final String TAG = "App";
    private static final String TELEGRAM_URL = "https://t.me/";

    private static final String BOT_PROPERTIES_FILENAME = "telegrambot.properties";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";

    private static final String SPOTIFY_PROPERTIES_FILENAME = "spotifyapp.properties";
    private static final String KEY_CLIENT_ID = "CLIENT_ID";
    private static final String KEY_CLIENT_SECRET = "CLIENT_SECRET";
    private static final String KEY_REDIRECT_URL = "REDIRECT_URL";

    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public static void main(String[] args) throws TelegramApiException, IOException {
        logger.info("Application started");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        logger.info("Reading configuration info from {}", SPOTIFY_PROPERTIES_FILENAME);
        Properties spotifyProperties = readProperties(classLoader, SPOTIFY_PROPERTIES_FILENAME);
        String clientId = spotifyProperties.getProperty(KEY_CLIENT_ID);
        String clientSecret = spotifyProperties.getProperty(KEY_CLIENT_SECRET);
        String redirectUrl = spotifyProperties.getProperty(KEY_REDIRECT_URL);

        logger.info("Initializing Spotify client");
        SpotifyClient.initialize(clientId, clientSecret, redirectUrl);

        logger.info("Reading configuration info from {}", BOT_PROPERTIES_FILENAME);
        Properties botProperties = readProperties(classLoader, BOT_PROPERTIES_FILENAME);
        String botUsername = botProperties.getProperty(KEY_BOT_USERNAME);
        String botToken = botProperties.getProperty(KEY_BOT_TOKEN);

        logger.info("Registering Telegram bot");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        LongPollingBot bot = new Bot(botUsername, botToken);
        botsApi.registerBot(bot);

        logger.info("Session started: {}", TELEGRAM_URL + botUsername);
    }

    private static Properties readProperties(ClassLoader classLoader, String filename) throws IOException {
        InputStream propertiesInputStream = classLoader.getResourceAsStream(filename);
        Properties properties = new Properties();
        properties.load(propertiesInputStream);
        return properties;
    }
}
