package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class Command {
    public abstract Answer execute();

    @Component
    public static class Factory {
        @Autowired
        private SpotifyClient spotifyClient;

        public Command create(String name) {
            return switch (name) {
                case (StartCommand.NAME) -> new StartCommand();
                case (LoginCommand.NAME) -> new LoginCommand(spotifyClient);
                default -> new UnknownCommand(name);
            };
        }
    }
}
