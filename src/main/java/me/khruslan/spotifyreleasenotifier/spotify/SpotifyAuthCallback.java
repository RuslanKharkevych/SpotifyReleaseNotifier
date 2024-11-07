package me.khruslan.spotifyreleasenotifier.spotify;

import me.khruslan.spotifyreleasenotifier.bot.BotConfig;
import me.khruslan.spotifyreleasenotifier.bot.message.MessageService;
import me.khruslan.spotifyreleasenotifier.user.User;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.user.UserMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

@RestController
public class SpotifyAuthCallback {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthCallback.class);

    private final BotConfig botConfig;
    private final SpotifyService spotifyService;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthCallback(
            BotConfig botConfig,
            SpotifyService spotifyService,
            MessageService messageService,
            UserService userService
    ) {
        this.botConfig = botConfig;
        this.spotifyService = spotifyService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("${spotify.app.redirectPath}")
    public RedirectView handleRedirect(
            @RequestParam(name = "state") String state,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "error", required = false) String error
    ) {
        var userMetadata = UserMetadata.fromAuthState(state);
        logger.debug("Handling Spotify auth redirect: userMetadata={}", userMetadata);
        var credentials = code != null ? spotifyService.getAuthCredentials(code) : null;

        if (credentials != null) {
            logger.debug("Successfully authenticated with Spotify");
            messageService.sendMessage(userMetadata.chatId(), "Logged in!");
            createUser(userMetadata, credentials);
        } else {
            logger.debug("Failed to authenticate with Spotify: error={}", error);
            messageService.sendMessage(userMetadata.chatId(), "Failed to log in");
        }

        return new RedirectView(botConfig.getAbsoluteUrl());
    }

    private void createUser(UserMetadata metadata, AuthorizationCodeCredentials credentials) {
        var user = new User(metadata, credentials);
        logger.debug("Creating new user: {}", user);
        userService.createUser(user);
    }
}
