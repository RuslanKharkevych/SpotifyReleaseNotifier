package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.answer.WelcomeAnswer;

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
