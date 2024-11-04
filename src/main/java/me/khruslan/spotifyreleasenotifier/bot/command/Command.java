package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class Command {
    public abstract Answer execute();

    // TODO: Move to a separate class
    @Component
    public static class Factory {
        @Autowired
        private SpotifyService spotifyService;

        public Command create(String name) {
            return switch (name) {
                case (StartCommand.NAME) -> new StartCommand();
                case (LoginCommand.NAME) -> new LoginCommand(spotifyService);
                default -> new UnknownCommand(name);
            };
        }
    }
}
