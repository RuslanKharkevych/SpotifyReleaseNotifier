package me.khruslan.spotifyreleasenotifier.auth;

import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

public record SpotifyCredentials(String accessToken, String refreshToken, long tokenExpirationTimestamp) {
    public static SpotifyCredentials create(AuthorizationCodeCredentials authCredentials, Clock clock) {
        var accessToken = authCredentials.getAccessToken();
        var refreshToken = authCredentials.getRefreshToken();
        var tokenExpirationTimestamp = clock.millis() + TimeUnit.SECONDS.toMillis(authCredentials.getExpiresIn());
        return new SpotifyCredentials(accessToken, refreshToken, tokenExpirationTimestamp);
    }
}
