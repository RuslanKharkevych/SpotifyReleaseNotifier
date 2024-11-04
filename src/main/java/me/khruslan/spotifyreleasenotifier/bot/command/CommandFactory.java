package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.user.metadata.UserMetadata;
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

    public Command create(String name, UserMetadata metadata) {
        return switch (name) {
            case (StartCommand.NAME) -> new StartCommand();
            case (LoginCommand.NAME) -> new LoginCommand(userService, spotifyService, metadata);
            default -> new UnknownCommand(name);
        };
    }
}
