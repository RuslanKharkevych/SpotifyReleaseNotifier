package me.khruslan.spotifyreleasenotifier.command;

import me.khruslan.spotifyreleasenotifier.answer.Answer;

public abstract class Command {
    public abstract Answer execute();

    public static class Factory {
        @SuppressWarnings("SwitchStatementWithTooFewBranches")
        public Command create(String name) {
            return switch (name) {
                case (StartCommand.NAME) -> new StartCommand();
                default -> new UnknownCommand(name);
            };
        }
    }
}
