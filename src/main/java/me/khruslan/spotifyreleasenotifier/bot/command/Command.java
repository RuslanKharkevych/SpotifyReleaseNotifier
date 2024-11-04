package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;

public abstract class Command {
    public abstract Answer execute();

    public static class Factory {
        public Command create(String name) {
            return switch (name) {
                case (StartCommand.NAME) -> new StartCommand();
                case (LoginCommand.NAME) -> new LoginCommand();
                default -> new UnknownCommand(name);
            };
        }
    }
}
