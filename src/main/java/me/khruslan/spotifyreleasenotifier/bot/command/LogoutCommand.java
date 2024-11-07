package me.khruslan.spotifyreleasenotifier.bot.command;

import me.khruslan.spotifyreleasenotifier.bot.message.Answer;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.user.UserMetadata;

public class LogoutCommand extends Command {
    public static final String NAME = "/logout";

    private final UserService userService;
    private final UserMetadata userMetadata;

    public LogoutCommand(UserService userService, UserMetadata userMetadata) {
        this.userService = userService;
        this.userMetadata = userMetadata;
    }

    @Override
    public Answer execute() {
        var userId = userMetadata.userId();
        if (userService.userExists(userId)) {
            userService.deleteUser(userId);
            return Answer.successfullyLoggedOut();
        } else {
            return Answer.notLoggedIn();
        }
    }
}
