package me.khruslan.spotifyreleasenotifier.release;

import me.khruslan.spotifyreleasenotifier.release.builder.ReleaseBuilder;
import me.khruslan.spotifyreleasenotifier.release.model.Release;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.telegram.TelegramService;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import me.khruslan.spotifyreleasenotifier.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// TODO: Improve logging
@Component
public class ReleaseNotifier {
    private static final long JOB_RATE_MILLIS = 12L * 60L * 60L * 1000L;
    private static final String SPOTIFY_URL_KEY = "spotify";

    private static final Logger logger = LoggerFactory.getLogger(ReleaseNotifier.class);

    private final UserService userService;
    private final SpotifyService spotifyService;
    private final TelegramService telegramService;

    @Autowired
    public ReleaseNotifier(UserService userService, SpotifyService spotifyService, TelegramService telegramService) {
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.telegramService = telegramService;
    }

    @Scheduled(fixedRate = JOB_RATE_MILLIS)
    public void checkForNewReleases() {
        logger.debug("Checking for new releases");

        for (var user : userService.getAllUsers()) {
            if (!authenticate(user)) return;
            var albums = getNewAlbums(user);
            var chatId = user.getTelegramCredentials().chatId();
            notifyAboutNewAlbums(chatId, albums);
            userService.updateUser(user);
        }
    }

    private boolean authenticate(User user) {
        logger.debug("Authenticating user: {}", user);
        var credentials = user.getSpotifyCredentials();

        if (credentials.tokenExpirationTimestamp() > System.currentTimeMillis()) {
            logger.debug("User credentials are up-to-date: {}", credentials);
            return true;
        }

        var refreshedCredentials = spotifyService.refreshAccessToken(credentials.refreshToken());
        if (refreshedCredentials != null) {
            user.setSpotifyCredentials(refreshedCredentials);
            return true;
        } else {
            return false;
        }
    }

    private List<AlbumSimplified> getNewAlbums(User user) {
        var accessToken = user.getSpotifyCredentials().accessToken();
        var releaseHistory = user.getReleaseHistory();
        var newAlbums = spotifyService.getNewAlbums(accessToken, releaseHistory.date());

        var ignoredAlbumIds = releaseHistory.releases().stream().map(Release::getAlbumId).toList();
        newAlbums.removeIf(album -> ignoredAlbumIds.contains(album.getId()));

        if (!newAlbums.isEmpty()) {
            var updatedHistory = updateReleaseHistory(user.getId(), releaseHistory, newAlbums);
            user.setReleaseHistory(updatedHistory);
        }

        return newAlbums;
    }

    private void notifyAboutNewAlbums(long chatId, List<AlbumSimplified> albums) {
        for (var album : albums) {
            var url = album.getExternalUrls().get(SPOTIFY_URL_KEY);
            telegramService.sendMessage(chatId, url);
        }
    }

    private ReleaseHistory updateReleaseHistory(long userId, ReleaseHistory history, List<AlbumSimplified> albums) {
        var releases = new ArrayList<>(albums.stream().map(album -> createRelease(userId, album.getId())).toList());
        if (history.date().isEqual(LocalDate.now())) releases.addAll(history.releases());
        return new ReleaseHistory(releases);
    }

    private Release createRelease(long userId, String albumId) {
        return new ReleaseBuilder()
                .setAlbumId(albumId)
                .setUserId(userId)
                .build();
    }
}
