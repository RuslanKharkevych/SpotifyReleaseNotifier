package me.khruslan.spotifyreleasenotifier;

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

    private static final Logger logger = LoggerFactory.getLogger(TAG);

    public static void main(String[] args) throws TelegramApiException, IOException {
        logger.info("Application started");

        logger.info("Reading configuration info from {}", BOT_PROPERTIES_FILENAME);
        Properties botProperties = readBotProperties();
        String botUsername = botProperties.getProperty(KEY_BOT_USERNAME);
        String botToken = botProperties.getProperty(KEY_BOT_TOKEN);

        logger.info("Registering Telegram bot");
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        LongPollingBot bot = new Bot(botUsername, botToken);
        botsApi.registerBot(bot);

        logger.info("Session started: {}", TELEGRAM_URL + botUsername);
    }

    private static Properties readBotProperties() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesInputStream = classLoader.getResourceAsStream(BOT_PROPERTIES_FILENAME);

        Properties properties = new Properties();
        properties.load(propertiesInputStream);
        return properties;
    }
}
