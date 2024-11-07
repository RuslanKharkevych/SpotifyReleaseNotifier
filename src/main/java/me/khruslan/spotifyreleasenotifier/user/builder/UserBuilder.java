package me.khruslan.spotifyreleasenotifier.user.builder;

import me.khruslan.spotifyreleasenotifier.release.model.ReleaseHistory;
import me.khruslan.spotifyreleasenotifier.user.model.User;
import me.khruslan.spotifyreleasenotifier.auth.SpotifyCredentials;
import me.khruslan.spotifyreleasenotifier.auth.TelegramCredentials;

public class UserBuilder {
    private final User user;

    public UserBuilder() {
        user = new User();
    }

    public UserBuilder setId(Long id) {
        user.setId(id);
        return this;
    }

    public UserBuilder setTelegramCredentials(TelegramCredentials telegramCredentials) {
        user.setTelegramCredentials(telegramCredentials);
        return this;
    }

    public UserBuilder setSpotifyCredentials(SpotifyCredentials spotifyCredentials) {
        user.setSpotifyCredentials(spotifyCredentials);
        return this;
    }

    public UserBuilder setReleaseHistory(ReleaseHistory releaseHistory) {
        user.setReleaseHistory(releaseHistory);
        return this;
    }

    public User build() {
        return user;
    }
}
