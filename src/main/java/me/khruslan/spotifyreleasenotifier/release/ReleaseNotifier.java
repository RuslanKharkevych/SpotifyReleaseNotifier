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

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class ReleaseNotifier {
    private static final long JOB_RATE_MILLIS = 12L * 60L * 60L * 1000L;
    private static final long TOKEN_VALIDITY_SPARE_TIME_MILLIS = 60L * 1000L;
    private static final String SPOTIFY_URL_KEY = "spotify";

    private static final Logger logger = LoggerFactory.getLogger(ReleaseNotifier.class);

    private final Clock clock;
    private final UserService userService;
    private final SpotifyService spotifyService;
    private final TelegramService telegramService;

    @Autowired
    public ReleaseNotifier(Clock clock, UserService userService, SpotifyService spotifyService,
                           TelegramService telegramService) {
        this.clock = clock;
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.telegramService = telegramService;
    }

    @Scheduled(fixedRate = JOB_RATE_MILLIS)
    public void checkForNewReleases() {
        logger.debug("Checking for new releases");

        for (var user : userService.getAllUsers()) {
            getNewAlbums(user);
        }
    }

    private void getNewAlbums(User user) {
        logger.debug("Fetching new albums: user={}", user);
        var releases = user.getReleaseHistory().releases();
        var accessToken = supplyAccessToken(user);
        var onPageLoaded = consumeLoadedPage(user);
        spotifyService.getAlbumsFromFollowedArtists(accessToken, onPageLoaded);
        dumpOldReleases(user, releases);
    }

    private Supplier<String> supplyAccessToken(User user) {
        return () -> {
            authenticate(user);
            return user.getSpotifyCredentials().accessToken();
        };
    }

    private void authenticate(User user) {
        logger.debug("Authenticating user: {}", user);
        var credentials = user.getSpotifyCredentials();

        if (credentials.tokenExpirationTimestamp() > clock.millis() + TOKEN_VALIDITY_SPARE_TIME_MILLIS) {
            logger.debug("Access token is valid: credentials={}", credentials);
        } else if (refreshToken(user)) {
            userService.updateUser(user);
        }
    }

    private boolean refreshToken(User user) {
        var credentials = user.getSpotifyCredentials();
        var refreshedCredentials = spotifyService.refreshAccessToken(credentials.refreshToken());

        if (refreshedCredentials != null) {
            user.setSpotifyCredentials(refreshedCredentials);
            logger.debug("Successfully refreshed access token");
            return true;
        } else {
            logger.debug("Failed to refresh access token");
            return false;
        }
    }

    private Consumer<List<AlbumSimplified>> consumeLoadedPage(User user) {
        return (albums) -> {
            var newAlbums = filterNewAlbums(albums, user.getReleaseHistory());

            if (!newAlbums.isEmpty()) {
                logger.debug("Fetched new albums: {}", newAlbums);
                var chatId = user.getTelegramCredentials().chatId();
                notifyAboutNewAlbums(chatId, newAlbums);
                appendNewReleases(user, newAlbums);
            }
        };
    }

    private List<AlbumSimplified> filterNewAlbums(List<AlbumSimplified> albums, ReleaseHistory history) {
        return albums.stream()
                .filter(album -> releasedAfter(album, history.date()) && !alreadyNotified(album, history.releases()))
                .toList();
    }

    private boolean releasedAfter(AlbumSimplified album, LocalDate lastCheckedDate) {
        try {
            var releaseDate = LocalDate.parse(album.getReleaseDate());
            return !releaseDate.isBefore(lastCheckedDate);
        } catch (DateTimeParseException e) {
            logger.debug("Failed to parse release date: album={}", album);
            return false;
        }
    }

    private boolean alreadyNotified(AlbumSimplified album, List<Release> releases) {
        return releases.stream()
                .map(Release::getAlbumId)
                .toList()
                .contains(album.getId());
    }

    private void notifyAboutNewAlbums(long chatId, List<AlbumSimplified> albums) {
        logger.debug("Notifying about new albums: chatId={}, albums={}", chatId, albums);

        for (var album : albums) {
            var url = album.getExternalUrls().get(SPOTIFY_URL_KEY);
            telegramService.sendMessage(chatId, url);
        }
    }

    private void appendNewReleases(User user, List<AlbumSimplified> albums) {
        var releases = createReleases(user.getId(), albums);
        updateReleaseHistory(user, (history -> {
            releases.addAll(history.releases());
            return history.copyWith(releases);
        }));
    }

    private void dumpOldReleases(User user, List<Release> oldReleases) {
        updateReleaseHistory(user, (history -> {
            var dateToday = LocalDate.now(clock);
            var releases = new ArrayList<>(history.releases());

            if (history.date().isBefore(dateToday)) {
                releases.removeAll(oldReleases);
            }

            return new ReleaseHistory(dateToday, releases);
        }));
    }

    private void updateReleaseHistory(User user, Function<ReleaseHistory, ReleaseHistory> transformation) {
        logger.debug("Updating release history: user={}", user);
        var currentHistory = user.getReleaseHistory();
        var updatedHistory = transformation.apply(currentHistory);
        user.setReleaseHistory(updatedHistory);
        userService.updateUser(user);
        logger.debug("Updated release history: {}", updatedHistory);
    }

    private List<Release> createReleases(long userId, List<AlbumSimplified> albums) {
        return albums.stream()
                .map(album -> createRelease(userId, album.getId()))
                .collect(Collectors.toList());
    }

    private Release createRelease(long userId, String albumId) {
        return new ReleaseBuilder()
                .setAlbumId(albumId)
                .setUserId(userId)
                .build();
    }
}
