package me.khruslan.spotifyreleasenotifier.telegram.message;

public class Answer {
    private final String message;

    private Answer(String message) {
        this.message = message;
    }

    public static Answer welcome() {
        return new Answer("Welcome!");
    }

    public static Answer authUrl(String url) {
        return new Answer(url);
    }

    public static Answer alreadyLoggedIn() {
        return new Answer("Already logged in!");
    }

    public static Answer notLoggedIn() {
        return new Answer("Not logged in!");
    }

    public static Answer successfullyLoggedOut() {
        return new Answer("Successfully logged out!");
    }

    public static Answer failedToLogOut() {
        return new Answer("Failed to logout!");
    }

    public static Answer unrecognizedCommand(String commandName) {
        return new Answer("Couldn't recognize command " + commandName);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Answer{message='" + message + "'}";
    }
}
