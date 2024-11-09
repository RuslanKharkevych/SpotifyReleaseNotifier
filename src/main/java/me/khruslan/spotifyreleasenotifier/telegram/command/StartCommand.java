package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;

public class StartCommand extends Command {
    public static final String NAME = "/start";

    @Override
    public String execute() {
        return Messages.WELCOME;
    }

    @Override
    public String toString() {
        return "StartCommand";
    }
}
