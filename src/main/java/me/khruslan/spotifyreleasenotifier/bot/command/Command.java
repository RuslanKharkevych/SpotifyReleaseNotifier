package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.answer.Answer;

public abstract class Command {
    public abstract Answer execute();
}
