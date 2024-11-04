package me.khruslan.spotifyreleasenotifier.bot.answer;

public class WelcomeAnswer extends Answer {
    @Override
    public String getMessage() {
        return "Welcome!";
    }

    @Override
    public String toString() {
        return "WelcomeAnswer";
    }
}
