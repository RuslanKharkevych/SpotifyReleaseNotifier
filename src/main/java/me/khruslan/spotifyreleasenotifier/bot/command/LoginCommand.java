package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.message.Answer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.user.UserMetadata;

public class LoginCommand extends Command {
    public static final String NAME = "/login";

    private final UserService userService;
    private final SpotifyService spotifyService;
    private final UserMetadata userMetadata;

    public LoginCommand(UserService userService, SpotifyService spotifyService, UserMetadata userMetadata) {
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.userMetadata = userMetadata;
    }

    @Override
    public Answer execute() {
        if (userService.userExists(userMetadata.userId())) {
            return Answer.alreadyLoggedIn();
        } else {
            var authState = userMetadata.toAuthState();
            var url = spotifyService.getAuthUrl(authState);
            return Answer.authUrl(url);
        }
    }

    @Override
    public String toString() {
        return "LoginCommand{userMetadata=" + userMetadata + "}";
    }
}
