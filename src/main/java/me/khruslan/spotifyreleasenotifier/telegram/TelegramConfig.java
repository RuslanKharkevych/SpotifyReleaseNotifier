package me.khruslan.spotifyreleasenotifier.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramConfig {
    private static final String TELEGRAM_URL = "https://t.me/";

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.username}")
    private String username;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(token);
    }

    public String getToken() {
        return token;
    }

    public String getAbsoluteUrl() {
        return TELEGRAM_URL + username;
    }
}
