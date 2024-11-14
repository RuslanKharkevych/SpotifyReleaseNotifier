package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;

public class HelpCommand extends Command {
    public static final String NAME = "/help";

    @Override
    public String execute() {
        return Messages.COMMANDS_LIST;
    }
}
