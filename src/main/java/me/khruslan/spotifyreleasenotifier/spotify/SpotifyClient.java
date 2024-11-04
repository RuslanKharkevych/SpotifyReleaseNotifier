package me.khruslan.spotifyreleasenotifier.spotify;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

@Component
public class SpotifyClient {
    private final SpotifyApi api;

    public SpotifyClient(
            @Value("${spotify.app.clientId}") String clientId,
            @Value("${spotify.app.clientSecret}") String clientSecret,
            @Value("${spotify.app.redirectUrl}") String redirectUrl
    ) {
        api = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(SpotifyHttpManager.makeUri(redirectUrl))
                .build();
    }

    public String getAuthorizationUrl() {
        AuthorizationCodeUriRequest request = api.authorizationCodeUri().build();
        URI uri = request.execute();
        return uri.toString();
    }
}
