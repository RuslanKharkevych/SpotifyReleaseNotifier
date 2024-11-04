package me.khruslan.spotifyreleasenotifier.bot.answer;

public class AlreadyLoggedInAnswer extends Answer {
    @Override
    public String getMessage() {
        return "Already logged in!";
    }

    @Override
    public String toString() {
        return "AlreadyLoggedInAnswer";
    }
}
