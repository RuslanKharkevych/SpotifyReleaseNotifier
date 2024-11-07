package me.khruslan.spotifyreleasenotifier.auth;

import me.khruslan.spotifyreleasenotifier.telegram.TelegramConfig;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.builder.UserBuilder;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SpotifyAuthCallback {
    private static final Logger logger = LoggerFactory.getLogger(SpotifyAuthCallback.class);

    private final TelegramConfig telegramConfig;
    private final TelegramService telegramService;
    private final SpotifyService spotifyService;
    private final UserService userService;

    @Autowired
    public SpotifyAuthCallback(TelegramConfig telegramConfig, TelegramService telegramService,
                               SpotifyService spotifyService, UserService userService) {
        this.telegramConfig = telegramConfig;
        this.spotifyService = spotifyService;
        this.telegramService = telegramService;
        this.userService = userService;
    }

    @GetMapping("${spotify.app.redirectPath}")
    public RedirectView handleRedirect(@RequestParam(name = "state") String state,
                                       @RequestParam(name = "code", required = false) String code,
                                       @RequestParam(name = "error", required = false) String error) {
        var telegramCredentials = TelegramCredentials.fromAuthState(state);
        logger.debug("Handling Spotify auth redirect: telegramCredentials={}", telegramCredentials);
        var spotifyCredentials = code != null ? spotifyService.getAuthCredentials(code) : null;

        if (spotifyCredentials != null) {
            logger.debug("Successfully authenticated with Spotify");
            // TODO: Fix hardcoded message
            telegramService.sendMessage(telegramCredentials.chatId(), "Logged in!");
            createUser(telegramCredentials, spotifyCredentials);
        } else {
            logger.debug("Failed to authenticate with Spotify: error={}", error);
            // TODO: Fix hardcoded message
            telegramService.sendMessage(telegramCredentials.chatId(), "Failed to log in");
        }

        return new RedirectView(telegramConfig.getAbsoluteUrl());
    }

    private void createUser(TelegramCredentials telegramCredentials, SpotifyCredentials spotifyCredentials) {
        var user = new UserBuilder()
                .setTelegramCredentials(telegramCredentials)
                .setSpotifyCredentials(spotifyCredentials)
                .build();

        logger.debug("Creating new user: {}", user);
        // TODO: Error handling
        userService.createUser(user);
    }
}
