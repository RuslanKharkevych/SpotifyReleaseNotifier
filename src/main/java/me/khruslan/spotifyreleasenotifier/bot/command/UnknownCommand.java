package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;
import me.khruslan.spotifyreleasenotifier.bot.answer.UnrecognizedCommandAnswer;

public class UnknownCommand extends Command {
    private final String name;

    public UnknownCommand(String name) {
        this.name = name;
    }

    @Override
    public Answer execute() {
        return new UnrecognizedCommandAnswer(name);
    }

    @Override
    public String toString() {
        return "UnknownCommand{name='" + name + "'}";
    }
}
