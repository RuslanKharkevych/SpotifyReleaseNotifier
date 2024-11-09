package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import me.khruslan.spotifyreleasenotifier.user.UserService;

public class LoginCommand extends Command {
    public static final String NAME = "/login";

    private final UserService userService;
    private final SpotifyService spotifyService;
    private final TelegramCredentials credentials;

    public LoginCommand(UserService userService, SpotifyService spotifyService, TelegramCredentials credentials) {
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.credentials = credentials;
    }

    @Override
    public String execute() {
        if (userService.userExists(credentials.userId())) {
            return Messages.LOGIN_BAD_STATE;
        } else {
            var authState = credentials.toAuthState();
            var url = spotifyService.getAuthUrl(authState);
            return String.format(Messages.LOGIN_URL, url);
        }
    }

    @Override
    public String toString() {
        return "LoginCommand{credentials=" + credentials + "}";
    }
}
