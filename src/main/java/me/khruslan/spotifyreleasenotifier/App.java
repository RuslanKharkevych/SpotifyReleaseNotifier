package me.khruslan.spotifyreleasenotifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource({"classpath:telegrambot.properties", "classpath:spotifyapp.properties"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
