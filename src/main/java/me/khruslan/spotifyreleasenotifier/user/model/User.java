package me.khruslan.spotifyreleasenotifier.user.model;

import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;
import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;

import java.util.Objects;

public class User {
    private Long id;
    private TelegramCredentials telegramCredentials;
    private SpotifyCredentials spotifyCredentials;
    private ReleaseHistory releaseHistory;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User user)) return false;

        return Objects.equals(id, user.id) &&
                Objects.equals(telegramCredentials, user.telegramCredentials) &&
                Objects.equals(spotifyCredentials, user.spotifyCredentials) &&
                Objects.equals(releaseHistory, user.releaseHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telegramCredentials, spotifyCredentials, releaseHistory);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", telegramCredentials=" + telegramCredentials +
                ", spotifyCredentials=" + spotifyCredentials +
                ", releaseHistory=" + releaseHistory +
                "}";
    }
}
