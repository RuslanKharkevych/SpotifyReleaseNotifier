package me.khruslan.spotifyreleasenotifier.telegram.command;

import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.telegram.message.Messages;
import me.khruslan.spotifyreleasenotifier.user.UserService;

public class StatusCommand extends Command {
    public static final String NAME = "/status";

    private final UserService userService;
    private final TelegramCredentials credentials;

    public StatusCommand(UserService userService, TelegramCredentials credentials) {
        this.userService = userService;
        this.credentials = credentials;
    }

    @Override
    public String execute() {
        if (userService.userExists(credentials.userId())) {
            return Messages.STATUS_LOGGED_IN;
        } else {
            return Messages.STATUS_LOGGED_OUT;
        }
    }
}
