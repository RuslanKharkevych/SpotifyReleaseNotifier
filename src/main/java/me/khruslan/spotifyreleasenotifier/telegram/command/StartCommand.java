package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Answer;

public class StartCommand extends Command {
    public static final String NAME = "/start";

    @Override
    public Answer execute() {
        return Answer.welcome();
    }

    @Override
    public String toString() {
        return "StartCommand";
    }
}
