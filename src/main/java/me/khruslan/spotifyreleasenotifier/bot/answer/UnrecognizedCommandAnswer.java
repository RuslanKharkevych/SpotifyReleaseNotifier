package me.khruslan.spotifyreleasenotifier.bot.answer;

public class UnrecognizedCommandAnswer extends Answer {
    private final String commandName;

    public UnrecognizedCommandAnswer(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String getMessage() {
        return "Couldn't recognize command " + commandName;
    }

    @Override
    public String toString() {
        return "UnrecognizedCommandAnswer{commandName='" + commandName + "'}";
    }
}
