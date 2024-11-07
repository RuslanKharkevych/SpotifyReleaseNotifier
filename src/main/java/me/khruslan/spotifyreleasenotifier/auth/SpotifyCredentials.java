package me.khruslan.spotifyreleasenotifier.auth;

import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.util.concurrent.TimeUnit;

public record SpotifyCredentials(String accessToken, String refreshToken, long tokenExpirationTimestamp) {
    public SpotifyCredentials(AuthorizationCodeCredentials authCredentials) {
        this(authCredentials.getAccessToken(), authCredentials.getRefreshToken(),
                calculateSpotifyTokenExpirationTimestamp(authCredentials.getExpiresIn()));
    }

    private static long calculateSpotifyTokenExpirationTimestamp(int expiresInSeconds) {
        return System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(expiresInSeconds);
    }
}
