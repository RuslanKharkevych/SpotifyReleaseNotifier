package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.answer.LoginUrlAnswer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;

public class LoginCommand extends Command {
    public static final String NAME = "/login";

    private final SpotifyService spotifyService;

    public LoginCommand(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @Override
    public Answer execute() {
        var url = spotifyService.getAuthorizationUrl();
        return new LoginUrlAnswer(url);
    }

    @Override
    public String toString() {
        return "LoginCommand";
    }
}
