package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Answer;

public class UnknownCommand extends Command {
    private final String name;

    public UnknownCommand(String name) {
        this.name = name;
    }

    @Override
    public Answer execute() {
        return Answer.unrecognizedCommand(name);
    }

    @Override
    public String toString() {
        return "UnknownCommand{name='" + name + "'}";
    }
}
