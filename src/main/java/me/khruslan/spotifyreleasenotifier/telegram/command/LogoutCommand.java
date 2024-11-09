package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
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
    public String execute() {
        var userId = credentials.userId();

        if (!userService.userExists(userId)) {
            return Messages.LOGOUT_BAD_STATE;
        } else if (userService.deleteUser(userId)) {
            return Messages.LOGOUT_SUCCESS;
        } else {
            return Messages.INTERNAL_ERROR;
        }
    }

    @Override
    public String toString() {
        return "LogoutCommand{credentials=" + credentials + "}";
    }
}
