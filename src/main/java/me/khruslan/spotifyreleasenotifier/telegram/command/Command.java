package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Answer;

public abstract class Command {
    public abstract Answer execute();
}
