package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.message.Answer;

public abstract class Command {
    public abstract Answer execute();
}
