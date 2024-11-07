package me.khruslan.spotifyreleasenotifier.notifier;

import me.khruslan.spotifyreleasenotifier.bot.message.MessageService;
import me.khruslan.spotifyreleasenotifier.spotify.SpotifyService;
import me.khruslan.spotifyreleasenotifier.user.User;
import me.khruslan.spotifyreleasenotifier.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReleaseNotifier {
    private static final long RATE_MILLIS = 12L * 60L * 60L * 1000L;
    private static final String SPOTIFY_URL_KEY = "spotify";

    private static final Logger logger = LoggerFactory.getLogger(ReleaseNotifier.class);

    private final UserService userService;
    private final SpotifyService spotifyService;
    private final MessageService messageService;

    @Autowired
    public ReleaseNotifier(UserService userService, SpotifyService spotifyService, MessageService messageService) {
        this.userService = userService;
        this.spotifyService = spotifyService;
        this.messageService = messageService;
    }

    @Scheduled(fixedRate = RATE_MILLIS)
    public void checkForNewReleases() {
        for (var user : userService.getAllUsers()) {
            refreshToken(user);
            var albums = getNewAlbums(user);
            notifyAboutNewAlbums(user.getTelegramChatId(), albums);
            user.setReleasesLastCheckedDate(LocalDate.now().toString());
            userService.updateUser(user);
        }
    }

    private void refreshToken(User user) {
        if (user.getSpotifyTokenExpirationTimestamp() > System.currentTimeMillis()) {
            var credentials = spotifyService.refreshAccessToken(user.getSpotifyRefreshToken());
            user.setCredentials(credentials);
        }
    }

    private List<AlbumSimplified> getNewAlbums(User user) {
        // TODO: Map in UserService
        var lastCheckedDateString = user.getReleasesLastCheckedDate();
        List<AlbumSimplified> newAlbums;

        try {
            var lastCheckedDate = LocalDate.parse(lastCheckedDateString);
            newAlbums = spotifyService.getNewAlbums(user.getSpotifyAccessToken(), lastCheckedDate);
        } catch (DateTimeParseException e) {
            logger.error("Failed to parse releasesLastCheckedDate: {}", lastCheckedDateString);
            newAlbums = new ArrayList<>();
        }

        var notifiedRecord = getOrCreateNotifiedRecord(user);
        var ignoredAlbumIds = Arrays.asList(notifiedRecord.getAlbumIds());
        newAlbums.removeIf(album -> ignoredAlbumIds.contains(album.getId()));

        var updatedRecord = updateNotifiedRecord(notifiedRecord, newAlbums);
        user.setNotifiedReleasesRecord(updatedRecord);

        return newAlbums;
    }

    private void notifyAboutNewAlbums(Long chatId, List<AlbumSimplified> albums) {
        for (var album : albums) {
            var url = album.getExternalUrls().get(SPOTIFY_URL_KEY);
            messageService.sendMessage(chatId, url);
        }
    }

    private NotifiedReleasesRecord getOrCreateNotifiedRecord(User user) {
        var record = user.getNotifiedReleasesRecord();

        if (record == null) {
            record = new NotifiedReleasesRecord(LocalDate.now());
        }

        return record;
    }

    private NotifiedReleasesRecord updateNotifiedRecord(NotifiedReleasesRecord record, List<AlbumSimplified> albums) {
        var today = LocalDate.now();
        var albumIds = albums.stream().map(AlbumSimplified::getId).toList();

        if (record.getDate().isEqual(today)) {
            record.appendAlbumIds(albumIds);
        } else {
            record = new NotifiedReleasesRecord(today, albumIds);
        }

        return record;
    }
}
