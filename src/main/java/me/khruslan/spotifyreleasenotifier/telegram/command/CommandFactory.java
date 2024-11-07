package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommandFactory {
    private final UserService userService;
    private final SpotifyService spotifyService;

    @Autowired
    public CommandFactory(UserService userService, SpotifyService spotifyService) {
        this.userService = userService;
        this.spotifyService = spotifyService;
    }

    public Command create(String name, TelegramCredentials credentials) {
        return switch (name) {
            case (StartCommand.NAME) -> new StartCommand();
            case (LoginCommand.NAME) -> new LoginCommand(userService, spotifyService, credentials);
            case (LogoutCommand.NAME) -> new LogoutCommand(userService, credentials);
            default -> new UnknownCommand(name);
        };
    }
}
