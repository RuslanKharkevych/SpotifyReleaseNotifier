package me.khruslan.spotifyreleasenotifier.command;

import me.khruslan.spotifyreleasenotifier.answer.Answer;
import me.khruslan.spotifyreleasenotifier.answer.WelcomeAnswer;

public class StartCommand extends Command {
    static final String NAME = "/start";

    @Override
    public Answer execute() {
        return new WelcomeAnswer();
    }

    @Override
    public String toString() {
        return "StartCommand";
    }
}
