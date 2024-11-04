package me.khruslan.spotifyreleasenotifier.spotify;

import me.khruslan.spotifyreleasenotifier.bot.BotConfig;
import me.khruslan.spotifyreleasenotifier.bot.message.MessageService;
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

    private final BotConfig botConfig;
    private final SpotifyService spotifyService;
    private final MessageService messageService;

    @Autowired
    public SpotifyAuthCallback(BotConfig botConfig, SpotifyService spotifyService, MessageService messageService) {
        this.botConfig = botConfig;
        this.spotifyService = spotifyService;
        this.messageService = messageService;
    }

    @GetMapping("${spotify.app.redirectPath}")
    public RedirectView handleRedirect(
            @RequestParam(name = "state") String chatId,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "error", required = false) String error
    ) {
        logger.debug("Handling Spotify auth redirect: chatId={}", chatId);
        if (code != null && spotifyService.authorize(code)) {
            logger.debug("Successfully authenticated with Spotify");
            messageService.sendMessage(Long.valueOf(chatId), "Logged in!");
        } else {
            logger.debug("Failed to authenticate with Spotify: error={}", error);
            messageService.sendMessage(Long.valueOf(chatId), "Failed to log in");
        }
        return new RedirectView(botConfig.getAbsoluteUrl());
    }
}
