package me.khruslan.spotifyreleasenotifier.spotify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.Objects;

public class SpotifyClient {
    private static final String TAG = "SpotifyClient";

    private final static Logger logger = LoggerFactory.getLogger(TAG);
    private static SpotifyClient instance;

    private final SpotifyApi api;

    private SpotifyClient(String clientId, String clientSecret, String redirectUrl) {
        api = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUrl))
                .build();
    }

    public static void initialize(String clientId, String clientSecret, String redirectUrl) {
        instance = new SpotifyClient(clientId, clientSecret, redirectUrl);
    }

    public static SpotifyClient getInstance() {
        Objects.requireNonNull(instance, "Instance hasn't been initialized");
        return instance;
    }

    public String getAuthorizationUrl() {
        AuthorizationCodeUriRequest request = api.authorizationCodeUri().build();
        URI uri = request.execute();
        logger.info("Obtained authorization URL: {}", uri);
        return uri.toString();
    }
}
