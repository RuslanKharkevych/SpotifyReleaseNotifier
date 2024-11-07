package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Answer;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;

public class LogoutCommand extends Command {
    public static final String NAME = "/logout";

    private final UserService userService;
    private final TelegramCredentials credentials;

    public LogoutCommand(UserService userService, TelegramCredentials credentials) {
        this.userService = userService;
        this.credentials = credentials;
    }

    @Override
    public Answer execute() {
        var userId = credentials.userId();

        if (userService.userExists(userId)) {
            userService.deleteUser(userId);
            return Answer.successfullyLoggedOut();
        } else {
            return Answer.notLoggedIn();
        }
    }

    @Override
    public String toString() {
        return "LogoutCommand{credentials=" + credentials + "}";
    }
}
