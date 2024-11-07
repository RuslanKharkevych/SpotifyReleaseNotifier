package me.khruslan.spotifyreleasenotifier.user.model;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;

// TODO: Implement toString
public class User {
    private Long id;
    private TelegramCredentials telegramCredentials;
    private SpotifyCredentials spotifyCredentials;
    private ReleaseHistory releaseHistory = new ReleaseHistory();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TelegramCredentials getTelegramCredentials() {
        return telegramCredentials;
    }

    public void setTelegramCredentials(TelegramCredentials telegramCredentials) {
        this.telegramCredentials = telegramCredentials;
    }

    public SpotifyCredentials getSpotifyCredentials() {
        return spotifyCredentials;
    }

    public void setSpotifyCredentials(SpotifyCredentials spotifyCredentials) {
        this.spotifyCredentials = spotifyCredentials;
    }

    public ReleaseHistory getReleaseHistory() {
        return releaseHistory;
    }

    public void setReleaseHistory(ReleaseHistory releaseHistory) {
        this.releaseHistory = releaseHistory;
    }
}
