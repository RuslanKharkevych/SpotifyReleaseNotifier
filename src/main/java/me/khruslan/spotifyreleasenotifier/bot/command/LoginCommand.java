package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.answer.LoginUrlAnswer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyClient;

public class LoginCommand extends Command {
    static final String NAME = "/login";

    private final SpotifyClient spotifyClient;

    public LoginCommand(SpotifyClient spotifyClient) {
        this.spotifyClient = spotifyClient;
    }

    @Override
    public Answer execute() {
        String url = spotifyClient.getAuthorizationUrl();
        return new LoginUrlAnswer(url);
    }

    @Override
    public String toString() {
        return "LoginCommand";
    }
}
