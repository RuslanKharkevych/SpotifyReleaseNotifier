package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.answer.LoginUrlAnswer;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyClient;

public class LoginCommand extends Command {
    static final String NAME = "/login";

    @Override
    public Answer execute() {
        String url = SpotifyClient.getInstance().getAuthorizationUrl();
        return new LoginUrlAnswer(url);
    }

    @Override
    public String toString() {
        return "LoginCommand";
    }
}
