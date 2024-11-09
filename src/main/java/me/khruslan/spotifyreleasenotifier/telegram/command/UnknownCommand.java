package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;

public class UnknownCommand extends Command {
    private final String name;

    public UnknownCommand(String name) {
        this.name = name;
    }

    @Override
    public String execute() {
        return String.format(Messages.UNRECOGNIZED_COMMAND, name);
    }

    @Override
    public String toString() {
        return "UnknownCommand{name='" + name + "'}";
    }
}
