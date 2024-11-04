package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {
    private final SpotifyService spotifyService;

    @Autowired
    public CommandFactory(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public Command create(String name, Long chatId) {
        return switch (name) {
            case (StartCommand.NAME) -> new StartCommand();
            case (LoginCommand.NAME) -> new LoginCommand(chatId, spotifyService);
            default -> new UnknownCommand(name);
        };
    }
}
